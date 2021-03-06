import { gotoMain } from "./gotoMain.js";
import showModal from "./errmodal.js";
const firstName = document.getElementById("address-first-name");
const lastName = document.getElementById("address-last-name");
const phone = document.getElementById("address-phone");
const addr = document.getElementById("address-address");
const city = document.getElementById("address-city");
const state = document.getElementById("address-state");
const zip = document.getElementById("address-zip");
const changeBtn = document.getElementById("address-change-btn");

const accountIconUsername = document.getElementById("account-icon-username");

const addrBackBtn = document.getElementById("address-back-btn");
const acctBackBtn = document.getElementById("account-back-btn");
/* * post request edit addr info * */

changeBtn.addEventListener("click", async (_) => {
  if (
    firstName.value === "" ||
    lastName.value === "" ||
    phone.value === "" ||
    addr.value === "" ||
    city.value === "" ||
    state.value === "" ||
    zip.value === ""
  ) {
    showModal("Please do not left empty fields");
    // alert("Please do not left empty fields");
    return;
  }
  if (!/^\d+$/.test(phone.value)) {
    showModal("Please enter valid phone number");
    // alert("Please enter valid phone number");
    return;
  }

  try {
    let addrInfo = new Object();
    addrInfo.firstName = firstName.value;
    addrInfo.lastName = lastName.value;
    addrInfo.telephone = phone.value;
    addrInfo.street = addr.value;
    addrInfo.city = city.value;
    addrInfo.state = state.value;
    addrInfo.postcode = zip.value;

    const url = `http://localhost:8080/test/user/${sessionStorage.getItem(
      "userID"
    )}/address`;

    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify(addrInfo),
    });
    const jsData = await response.text();

    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      accountIconUsername.innerText =
        "Hello, " + firstName.value + " " + lastName.value;
      showModal("Changed Successfully!");
      // alert("Changed Successfully!");
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});

addrBackBtn.addEventListener("click", gotoMain);
acctBackBtn.addEventListener("click", gotoMain);
/* * get request get addr info * */
