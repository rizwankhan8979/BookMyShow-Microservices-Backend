/**
 * Payment Gateway JavaScript Module
 * Course: Rizwan 2.0
 * 
 * NOTE FOR DEVELOPER / USER:
 * This script contains the UI interaction logic and a commented fetch function 
 * showing how to connect your backend Spring Boot API (/api/payment/create-order).
 */

// Global State / Prices
let basePrice = 2.00;
let appliedDiscount = 0;

/**
 * Toggle Coupon Accordion
 */
function toggleCouponAccordion() {
    const couponCard = document.querySelector('.coupon-card');
    couponCard.classList.toggle('open');
}

/**
 * Apply Coupon Code Logic
 */
function applyCoupon() {
    const couponInput = document.getElementById('couponCode');
    const couponMessage = document.getElementById('couponMessage');
    const code = couponInput.value.trim().toUpperCase();

    if (!code) {
        couponMessage.className = 'coupon-message error';
        couponMessage.textContent = 'Please enter a coupon code.';
        return;
    }

    // Demo Coupon Logic (e.g. RIZWAN50 gives 10% discount)
    if (code === 'RIZWAN50' || code === 'EARLY500') {
        let discountVal = (code === 'RIZWAN50') ? 459 : 500;
        appliedDiscount = discountVal;
        
        let newAmount = basePrice - appliedDiscount;
        if (newAmount < 0) newAmount = 0;

        // Update UI Prices
        document.getElementById('discountedPrice').textContent = `₹${newAmount.toFixed(2)}`;
        document.getElementById('finalAmount').textContent = `₹${newAmount.toFixed(2)}`;
        document.getElementById('btnText').textContent = `Proceed to pay ₹${newAmount.toFixed(2)}`;

        couponMessage.className = 'coupon-message success';
        couponMessage.textContent = `Coupon "${code}" applied successfully! You saved ₹${discountVal}.`;
    } else {
        couponMessage.className = 'coupon-message error';
        couponMessage.textContent = 'Invalid coupon code. Try "RIZWAN50".';
    }
}

/**
 * Form Submit Handler (Create Order Flow)
 */
function handleFormSubmit(event) {
    event.preventDefault();

    // 1. Extract Form Values
    const fullName = document.getElementById('fullName').value.trim();
    const email = document.getElementById('email').value.trim();
    const phoneNumber = document.getElementById('phoneNumber').value.trim();
    const courseName = document.getElementById('courseName').textContent.trim(); // "Rizwan 2.0"
    
    // Calculate final numeric amount
    const finalAmountVal = basePrice - appliedDiscount;

    // Basic Validation
    if (!fullName || !email || !phoneNumber) {
        showAlert('Please fill in all required billing details.', 'error');
        return;
    }

    if (phoneNumber.length !== 10 || isNaN(phoneNumber)) {
        showAlert('Please enter a valid 10-digit mobile number.', 'error');
        return;
    }

    // 2. Prepare Payload Object matching backend PaymentOrder Entity
    const payload = {
        name: fullName,
        email: email,
        phoneNumber: phoneNumber,
        courseName: courseName,
        amount: finalAmountVal
    };

    console.log('Sending Payload to Backend:', payload);

    // Show Loading State on Button
    setButtonLoading(true);

    /* =========================================================================
       BACKEND INTEGRATION CODE (Aap is section se backend connect kar sakte hain):
       
       Aap apne backend `/api/payment/create-order` endpoint par 
       niche diye gaye code ko uncomment kar ke connect kar sakte hain:
       ========================================================================= */

    
    // API Endpoint targeting Spring Boot running on port 8081
    const apiEndpoint = 'http://localhost:8081/api/payment/create-order';

    fetch(apiEndpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(res => res.json())
    .then(orderData => {
        console.log('Order Successfully Created:', orderData);

        // Options for Razorpay Checkout Modal UI
        const options = {
            "key": "rzp_test_TLYdnocq1Qjz6G", // Your Razorpay Test Key ID
            "amount": orderData.amount,      // Amount in paise
            "currency": "INR",
            "name": "Rizwan 2.0",
            "description": "Course Registration Payment",
            "order_id": orderData.id,        // Razorpay Order ID returned by backend
            "prefill": {
                "name": payload.name,
                "email": payload.email,
                "contact": payload.phoneNumber
            },
            "theme": {
                "color": "#E50914" // Red theme matching website button
            },
            "handler": function (response) {
                console.log('Razorpay Payment Success Response:', response);
                
                // Call Backend /api/payment/update-order API to update payment status to SUCCESS
                const updateUrl = `http://localhost:8081/api/payment/update-order?paymentId=${response.razorpay_payment_id}&orderId=${response.razorpay_order_id}&status=SUCCESS`;
                
                fetch(updateUrl, { method: 'POST' })
                .then(res => res.text())
                .then(msg => {
                    console.log('Order Update Status:', msg);
                    setButtonLoading(false);
                    showAlert(`🎉 Order Placed Successfully! Payment ID: ${response.razorpay_payment_id}`, 'success');
                })
                .catch(err => {
                    console.error('Update Order Error:', err);
                    setButtonLoading(false);
                    showAlert(`Payment Received (ID: ${response.razorpay_payment_id}), but DB update failed.`, 'success');
                });
            },
            "modal": {
                "ondismiss": function() {
                    console.log('Razorpay Checkout Modal closed by user');
                    setButtonLoading(false);
                }
            }
        };

        // Open Razorpay Payment UI Modal Window
        const rzp = new Razorpay(options);
        rzp.open();
    })
    .catch(error => {
        console.error('API Error:', error);
        setButtonLoading(false);
        showAlert('Order creation failed! Please check backend server status.', 'error');
    });

}

/**
 * Toggle Button Loading State
 */
function setButtonLoading(isLoading) {
    const proceedBtn = document.getElementById('proceedBtn');
    if (isLoading) {
        proceedBtn.classList.add('loading');
        proceedBtn.disabled = true;
    } else {
        proceedBtn.classList.remove('loading');
        proceedBtn.disabled = false;
    }
}

/**
 * Show Temporary Response Alert
 */
function showAlert(message, type) {
    const alertBox = document.getElementById('responseAlert');
    alertBox.className = `response-alert ${type}`;
    alertBox.textContent = message;
    alertBox.scrollIntoView({ behavior: 'smooth', block: 'end' });

    // Auto hide after 8 seconds
    setTimeout(() => {
        alertBox.className = 'response-alert hidden';
    }, 8000);
}
