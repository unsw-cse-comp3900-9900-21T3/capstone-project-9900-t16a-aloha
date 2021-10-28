const checkoutBtn = document.getElementById("check-out-btn");
const itemsList = document.getElementById("shopping-cart-list");
const checkoutAddr = document.getElementById("checkout-address");

// addr input
const firstName = document.getElementById("check-first-name");
const lastName = document.getElementById("check-last-name");
const phone = document.getElementById("check-phone");
const address = document.getElementById("check-address");
const city = document.getElementById("check-city");
const state = document.getElementById("check-state");
const zip = document.getElementById("check-zip");
const shoppingcartParent = document.getElementById("shopping-cart-list");
checkoutBtn.addEventListener("click", async () => {
  if (shoppingcartParent.childNodes.length === 0) {
    alert("No items in shopping cart");
    return;
  }
  itemsList.style.display = "none";
  checkoutAddr.style.display = "block";

  try {
    const url = `http://localhost:8080/test/user/${sessionStorage.getItem(
      "userID"
    )}/address`;

    const response = await fetch(url, {
      method: "get",
      headers: {
        "content-type": "application/json",
      },
    });
    const jsData = await response.text();
    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      checkoutBtn.style.display = "none";

      firstName.value = d.firstname;
      lastName.value = d.lastname;
      phone.value = d.telephone;
      address.value = d.street;
      city.value = d.city;
      state.value = d.state;
      zip.value = d.postcode;
    }
    const money = document.getElementById("total-amount-check").innerText;
    const amt = money.substring(1);
    initPayPalButton(parseFloat(amt));
    sessionStorage.setItem("totalAmt", amt);
  } catch (err) {
    console.log(err);
  }
});

function initPayPalButton(moneyAmt) {
  paypal
    .Buttons({
      style: {
        shape: "rect",
        color: "black",
        layout: "vertical",
        label: "paypal",
      },

      createOrder: function (data, actions) {
        return actions.order.create({
          purchase_units: [
            { amount: { currency_code: "AUD", value: moneyAmt } },
          ],
        });
      },

      onApprove: function (data, actions) {
        return actions.order.capture().then(function (orderData) {
          // Full available details
          console.log(
            "Capture result",
            orderData,
            JSON.stringify(orderData, null, 2)
          );

          // Show a success message within this page, e.g.
          //   const element = document.getElementById("paypal-button-container");
          //   element.innerHTML = "";
          //   element.innerHTML = "<h3>Thank you for your payment!</h3>";
          actions.redirect("http://localhost:7999/finished.html");
          // Or go to another URL:  actions.redirect('thank_you.html');
        });
      },

      onError: function (err) {
        console.log(err);
      },
    })
    .render("#paypal-button-container");
}

// if (
//     firstName.value === "" ||
//     lastName.value === "" ||
//     phone.value === "" ||
//     addr.value === "" ||
//     city.value === "" ||
//     state.value === "" ||
//     zip.value === ""
//   ) {
//     alert("Please do not left empty fields");
//     return;
//   }
//   if (!/^\d+$/.test(phone.value)) {
//     alert("Please enter valid phone number");
//     return;
//   }
