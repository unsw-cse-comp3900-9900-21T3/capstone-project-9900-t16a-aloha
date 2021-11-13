import { preLoadAddr, preLoadAcct, preLoadOrder } from "./account.js";
import { gotoMain } from "./gotoMain.js";
import { getUserSearchProductList } from "./userSearchPage.js";

import { getProductDetail } from "./gotoUserProductDetailPage.js";
// nav bars element

const navDropdownAddr = document.getElementById("nav-dropdown-addr");
const navDropdownAcct = document.getElementById("nav-dropdown-acct");
const navDropdownOrder = document.getElementById("nav-dropdown-order");

const userSearchPageLink = document.getElementById("user-search-page-link");
const logoutBtn = document.getElementsByClassName("nav-dropdown-logout");
const aboutLink = document.getElementById("user-about-page-link");
// admin/user header
const adminHeader = document.getElementById("admin-header");
const userHeader = document.getElementById("user-header");

// pages DOM
const accountPage = document.getElementById("account-page");
const searchPage = document.getElementById("product-search-page");
const acctAddrPage = document.getElementById("address-subpage");
const acctAcctPage = document.getElementById("accout-subpage");
const acctOrderPage = document.getElementById("order-subpage");

const recommendPage = document.getElementById("recommend-page");
const userSearchPage = document.getElementById("user-search-page");
const userSearchBar = document.getElementById("user-search-bar");
const userAboutPage = document.getElementById("user-about-page");
const userProductDetailPage = document.getElementById(
  "user-product-detail-page"
);
// main page init

// go to account Addr
const gotoAccountAddr = () => {
  acctOrderPage.style.display = "none";
  acctAddrPage.style.display = "block";
  acctAcctPage.style.display = "none";
  searchPage.style.display = "none";
  accountPage.style.display = "block";
  recommendPage.style.display = "none";
  userSearchPage.style.display = "none";
  userSearchBar.style.display = "none";
  userAboutPage.style.display = "none";
  userProductDetailPage.style.display = "none";

  preLoadAddr();
};

// go to account account
const gotoAccountAcct = () => {
  acctOrderPage.style.display = "none";
  acctAddrPage.style.display = "none";
  acctAcctPage.style.display = "block";
  searchPage.style.display = "none";
  accountPage.style.display = "block";
  recommendPage.style.display = "none";
  userSearchPage.style.display = "none";
  userSearchBar.style.display = "none";

  userProductDetailPage.style.display = "none";
  userAboutPage.style.display = "none";
  preLoadAcct();
};

// go to account account
const gotoAccountOrder = () => {
  acctOrderPage.style.display = "block";
  acctAddrPage.style.display = "none";
  acctAcctPage.style.display = "none";
  searchPage.style.display = "none";
  accountPage.style.display = "block";
  recommendPage.style.display = "none";
  userSearchPage.style.display = "none";
  userSearchBar.style.display = "none";

  userProductDetailPage.style.display = "none";
  userAboutPage.style.display = "none";
  preLoadOrder();
};

const gotoUserSearchPage = () => {
  userSearchPage.style.display = "block";
  userSearchBar.style.display = "block";
  getUserSearchProductList();

  userProductDetailPage.style.display = "none";
  acctAddrPage.style.display = "none";
  acctAcctPage.style.display = "none";
  acctOrderPage.style.display = "none";
  searchPage.style.display = "none";
  accountPage.style.display = "none";
  userAboutPage.style.display = "none";
  recommendPage.style.display = "none";
};

const gotoUserProductDetailPageDom = (pid) => {
  getProductDetail(pid);

  userSearchPage.style.display = "none";
  userSearchBar.style.display = "none";
  acctAddrPage.style.display = "none";
  acctOrderPage.style.display = "none";
  acctAcctPage.style.display = "none";
  searchPage.style.display = "none";
  accountPage.style.display = "none";
  userAboutPage.style.display = "none";
  recommendPage.style.display = "none";

  userProductDetailPage.style.display = "block";
};

const gotoAbout = () => {
  userAboutPage.style.display = "block";

  userSearchPage.style.display = "none";
  acctOrderPage.style.display = "none";
  userSearchBar.style.display = "none";
  acctAddrPage.style.display = "none";
  acctAcctPage.style.display = "none";
  searchPage.style.display = "none";
  accountPage.style.display = "none";
  recommendPage.style.display = "none";
};

const gotoLogin = () => {
  sessionStorage.clear();
  location.href = "./index.html";
};

for (let ele of logoutBtn) {
  ele.addEventListener("click", gotoLogin);
}
navDropdownOrder.addEventListener("click", gotoAccountOrder);
navDropdownAddr.addEventListener("click", gotoAccountAddr);
navDropdownAcct.addEventListener("click", gotoAccountAcct);
userSearchPageLink.addEventListener("click", gotoUserSearchPage);
aboutLink.addEventListener("click", gotoAbout);
export { gotoUserProductDetailPageDom };
