import { preLoadAddr, preLoadAcct } from "./account.js";
import { gotoMain } from "./gotoMain.js";
// nav bars element
const mainBtn = document.getElementById("left-top-main");
const navDropdownAddr = document.getElementById("nav-dropdown-addr");
const navDropdownAcct = document.getElementById("nav-dropdown-acct");
const logoutBtn = document.getElementsByClassName("nav-dropdown-logout");

// admin/user header
const adminHeader = document.getElementById("admin-header");
const userHeader = document.getElementById("user-header");

// pages DOM
const accountPage = document.getElementById("account-page");
const searchPage = document.getElementById("product-search-page");
const acctAddrPage = document.getElementById("address-subpage");
const acctAcctPage = document.getElementById("accout-subpage");

// main page init

mainBtn.addEventListener("click", gotoMain);

// go to account Addr
const gotoAccountAddr = () => {
  acctAddrPage.style.display = "block";
  acctAcctPage.style.display = "none";
  searchPage.style.display = "none";
  accountPage.style.display = "block";
  preLoadAddr();
};

// go to account account
const gotoAccountAcct = () => {
  acctAddrPage.style.display = "none";
  acctAcctPage.style.display = "block";
  searchPage.style.display = "none";
  accountPage.style.display = "block";
  preLoadAcct();
};

const gotoLogin = () => {
  sessionStorage.clear();
  location.href = "./index.html";
};

for (let ele of logoutBtn) {
  ele.addEventListener("click", gotoLogin);
}

navDropdownAddr.addEventListener("click", gotoAccountAddr);
navDropdownAcct.addEventListener("click", gotoAccountAcct);
