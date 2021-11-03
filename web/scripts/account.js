import { getAllOrders } from "./orderHistory.js";
const accountAddressBtn = document.getElementById("account-address-btn");
const accountAccountBtn = document.getElementById("account-account-btn");
const accountOrderBtn = document.getElementById("account-order-btn");

const accountIconUsername = document.getElementById("account-icon-username");

const acctAddrPage = document.getElementById("address-subpage");
const acctAcctPage = document.getElementById("accout-subpage");
const acctOrderPage = document.getElementById("order-subpage");

const acctEmail = document.getElementById("account-email");

const acctFName = document.getElementById("account-first-name");
const acctLName = document.getElementById("account-last-name");
const preLoadAcct = async () => {
  try {
    const url = `http://localhost:8080/test/user/${sessionStorage.getItem(
      "userID"
    )}/info`;

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
      acctEmail.value = d.email;
      // acctPass =
      acctFName.value = d.firstname;
      acctLName.value = d.lastname;
      accountIconUsername.innerText =
        "Hello, " + d.firstname + " " + d.lastname;
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

const preLoadAddr = async () => {
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
      const firstName = document.getElementById("address-first-name");
      const lastName = document.getElementById("address-last-name");
      const phone = document.getElementById("address-phone");
      const addr = document.getElementById("address-address");
      const city = document.getElementById("address-city");
      const state = document.getElementById("address-state");
      const zip = document.getElementById("address-zip");
      firstName.value = d.firstname;
      lastName.value = d.lastname;
      phone.value = d.telephone;
      addr.value = d.street;
      city.value = d.city;
      state.value = d.state;
      zip.value = d.postcode;
      accountIconUsername.innerText =
        "Hello, " + d.firstname + " " + d.lastname;
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

const preLoadOrder = ()=>{
  console.log("preloadorder");
  getAllOrders();
}
export { preLoadAddr, preLoadAcct ,preLoadOrder};
preLoadAddr();
preLoadAcct();

const gotoAddr = () => {
  acctAddrPage.style.display = "block";
  acctAcctPage.style.display = "none";
  acctOrderPage.style.display = "none";
  preLoadAddr();
};
const gotoAcct = () => {
  acctAcctPage.style.display = "block";
  acctAddrPage.style.display = "none";
  acctOrderPage.style.display = "none";
  preLoadAcct();
};


const gotoOrder = () =>{
  acctAcctPage.style.display = "none";
  acctAddrPage.style.display = "none";
  acctOrderPage.style.display = "block";
  preLoadOrder();
}

accountAddressBtn.addEventListener("click", gotoAddr);
accountAccountBtn.addEventListener("click", gotoAcct);
accountOrderBtn.addEventListener("click", gotoOrder);
// Account info POST request

const acctChangeBtn = document.getElementById("account-change-btn");
acctChangeBtn.addEventListener("click", async (_) => {
  if (
    acctFName.value === "" ||
    acctLName.value === "" ||
    acctEmail.value === ""
  ) {
    alert("Please do not left empty fields");
    return;
  }
  if (!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(acctEmail.value)) {
    alert("Please enter valid email address");
    return;
  }

  try {
    let acctInfo = new Object();
    console.log(acctFName.value);
    acctInfo.firstName = acctFName.value;
    acctInfo.lastName = acctLName.value;
    acctInfo.email = acctEmail.value;
    const url = `http://localhost:8080/test/user/${sessionStorage.getItem(
      "userID"
    )}/account`;

    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify(acctInfo),
    });
    const jsData = await response.text();

    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      accountIconUsername.innerText =
        "Hello, " + acctFName.value + " " + acctLName.value;
      alert("Changed Successfully!");
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});
