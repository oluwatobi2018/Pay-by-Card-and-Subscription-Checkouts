SQUARE_ACCESS_TOKEN={SANDBOX_ACCESS_TOKEN}
<form id="payment-form">
<div id="card-container"></div>
<button id="card-button" type="button">Pay $1.00</button>
</form>
<div id="payment-status-container"></div>
<script>
</script>
const appId = '{YOUR_SANDBOX_APPLICATION_ID}';
const locationId = '{YOUR_SANDBOX_LOCATION_ID}'; 
async function initializeCard(payments) {
    const card = await payments.card();
    await card.attach('#card-container'); 
    return card; 
  }
 
 document.addEventListener('DOMContentLoaded', async function () {
   if (!window.Square) {
     throw new Error('Square.js failed to load properly');
   }
   const payments = window.Square.payments(appId, locationId);
   let card;
   try {
     card = await initializeCard(payments);
   } catch (e) {
     console.error('Initializing Card failed', e);
     return;
   }
 
   // Step 5.2: create card payment
 }); // Call this function to send a payment token, buyer name, and other details
 // to the project server code so that a payment can be created with 
 // Payments API
 async function createPayment(token) {
   const body = JSON.stringify({
     locationId,
     sourceId: token,
   });
   const paymentResponse = await fetch('/payment', {
     method: 'POST',
     headers: {
       'Content-Type': 'application/json',
     },
     body,
   });
   if (paymentResponse.ok) {
     return paymentResponse.json();
   }
   const errorBody = await paymentResponse.text();
   throw new Error(errorBody);
 }

 // This function tokenizes a payment method. 
 // The ‘error’ thrown from this async function denotes a failed tokenization,
 // which is due to buyer error (such as an expired card). It is up to the
 // developer to handle the error and provide the buyer the chance to fix
 // their mistakes.
 async function tokenize(paymentMethod) {
   const tokenResult = await paymentMethod.tokenize();
   if (tokenResult.status === 'OK') {
     return tokenResult.token;
   } else {
     let errorMessage = `Tokenization failed-status: ${tokenResult.status}`;
     if (tokenResult.errors) {
       errorMessage += ` and errors: ${JSON.stringify(
         tokenResult.errors
       )}`;
     }
     throw new Error(errorMessage);
   }
 }

 // Helper method for displaying the Payment Status on the screen.
 // status is either SUCCESS or FAILURE;
 function displayPaymentResults(status) {
   const statusContainer = document.getElementById(
     'payment-status-container'
   );
   if (status === 'SUCCESS') {
     statusContainer.classList.remove('is-failure');
     statusContainer.classList.add('is-success');
   } else {
     statusContainer.classList.remove('is-success');
     statusContainer.classList.add('is-failure');
   }

   statusContainer.style.visibility = 'visible';
 }    
 async function handlePaymentMethodSubmission(event, paymentMethod) {
    event.preventDefault();
 
    try {
      // disable the submit button as we await tokenization and make a
      // payment request.
      cardButton.disabled = true;
      const token = await tokenize(paymentMethod);
      const paymentResults = await createPayment(token);
      displayPaymentResults('SUCCESS');
 
      console.debug('Payment Success', paymentResults);
    } catch (e) {
      cardButton.disabled = false;
      displayPaymentResults('FAILURE');
      console.error(e.message);
    }
  }
 
  const cardButton = document.getElementById(
    'card-button'
  );
  cardButton.addEventListener('click', async function (event) {
    await handlePaymentMethodSubmission(event, card);
  });
  document.addEventListener('DOMContentLoaded', async function () {
    if (!window.Square) {
      throw new Error('Square.js failed to load properly');
    }
  
    const payments = window.Square.payments(appId, locationId);
    let card;
    try {
      card = await initializeCard(payments);
    } catch (e) {
      console.error('Initializing Card failed', e);
      return;
    }
  
    // Checkpoint 2.
    async function handlePaymentMethodSubmission(event, paymentMethod) {
      event.preventDefault();
  
      try {
        // disable the submit button as we await tokenization and make a
        // payment request.
        cardButton.disabled = true;
        const token = await tokenize(paymentMethod);
        const paymentResults = await createPayment(token);
        displayPaymentResults('SUCCESS');
  
        console.debug('Payment Success', paymentResults);
      } catch (e) {
        cardButton.disabled = false;
        displayPaymentResults('FAILURE');
        console.error(e.message);
      }
    }
  
    const cardButton = document.getElementById(
      'card-button'
    );
    cardButton.addEventListener('click', async function (event) {
      await handlePaymentMethodSubmission(event, card);
    });
  });
  curl https://connect.squareupsandbox.com/v2/online-checkout/payment-links \
  -X POST \
  -H 'Square-Version: 2022-08-23' \
  -H 'Authorization: Bearer {ACCESS_TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
    "idempotency_key": "{UNIQUE_KEY}",
    "quick_pay": {
      "name": "Auto Detailing",
      "price_money": {
        "amount": 12500,
        "currency": "USD"
      },
      "location_id": "{LOCATION_ID}"
    }
  }'
  {
    "payment_link": {
      "id": "FWT463MU2JIG7S3D",
      "version": 1,
      "order_id": "C0DMgui6YFmgyURVSRtxr4EShheZY",
      "url": "https://sandbox.square.link/u/jUjglZiR",
      "created_at": "2022-04-23T18:54:40Z"
    }
  }
  curl https://connect.squareupsandbox.com/v2/catalog/object \
  -X POST \
  -H 'Square-Version: 2022-08-23' \
  -H 'Authorization: Bearer {ACCESS_TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
    "idempotency_key": "{UNIQUE_KEY}",
    "object": {
      "type": "SUBSCRIPTION_PLAN",
      "id": "#plan",
      "subscription_plan_data": {
        "name": "One-phase subscription plan for testing checkout  link.",
        "phases": [
          {
            "cadence": "WEEKLY",
            "recurring_price_money": {
              "amount": 1500,
              "currency": "USD"
            }
          }
        ]
      }
    }
  }'
  curl https://connect.squareupsandbox.com/v2/online-checkout/payment-links \
  -X POST \
  -H 'Square-Version: 2022-08-23' \
  -H 'Authorization: Bearer {ACCESS_TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
    "idempotency_key": "{UNIQUE_KEY}",
    "quick_pay": {
      "name": "Gym membership fees",
      "price_money": {
        "amount": 1500,
        "currency": "USD"
      },
      "location_id": "7WQ0KXC8ZSD90"
    },
    "checkout_options": {
      "subscription_plan_id": "{SUBSCRIPTION_PLAN_ID}"
    }
  }'
  {
    "payment_link": {
      "id": "OTGMWB3A65HJW2WF",
      "version": 1,
      "description": "",
      "order_id": "E1Ho92Z8lquV9QRIiQ5MWLxKw6MZY",
      "checkout_options": {
        "subscription_plan_id": "TF2EBYIBAM6BJTLBNYPAXD2X"
      },
      "url": "https://sandbox.square.link/u/Kzc5PBJq",
      "created_at": "2022-04-23T21:23:34Z"
    }
  }
  
 
