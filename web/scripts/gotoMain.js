import { getProductList } from "./adminProductList.js";
import { getRecommend } from "./getRecommend.js";
// admin/user header
const adminHeader = document.getElementById("admin-header");
const userHeader = document.getElementById("user-header");

// pages DOM
const accountPage = document.getElementById("account-page");
const searchPage = document.getElementById("product-search-page");
const searchBar = document.getElementById("search-bar");
const addProductPage = document.getElementById("add-product-detail-page");
const recommendPage = document.getElementById("recommend-page");

const userSearchPage = document.getElementById("user-search-page");
const userSearchBar = document.getElementById("user-search-bar");
const userAboutPage = document.getElementById("user-about-page");
const userProductDetailPage = document.getElementById(
  "user-product-detail-page"
);
const accessMenu = document.getElementById("accessibility-menu");
const chatBot = document.getElementById("chat-bot");
const gotoMain = () => {
  accountPage.style.display = "none";
  addProductPage.style.display = "none";
  userProductDetailPage.style.display = "none";
  userAboutPage.style.display = "none";
  userSearchBar.style.display = "none";
  userSearchPage.style.display = "none";
  if (sessionStorage.getItem("isAdmin") == 1) {
    getProductList();

    adminHeader.style.display = "block";
    userHeader.style.display = "none";
    searchPage.style.display = "block";
    searchBar.style.display = "block";
    recommendPage.style.display = "none";
    accessMenu.style.display = "none";
    chatBot.style.display = "none";
  } else {
    // customer
    // 1. show recommend
    getRecommend();
    adminHeader.style.display = "none";
    userHeader.style.display = "block";
    searchPage.style.display = "none";
    searchBar.style.display = "none";
    recommendPage.style.display = "block";
    accessMenu.style.display = "block";
    chatBot.style.display = "block";
  }
};
gotoMain();

const backBtn = document.getElementById("user-product-page-back-btn");
backBtn.addEventListener("click", gotoMain);
const mainBtn = document.getElementById("left-top-main");
mainBtn.addEventListener("click", gotoMain);
const justForYouLink = document.getElementById("user-main-page-link");
justForYouLink.addEventListener("click", gotoMain);
// add product detail
// searchPage.style.display = "none";
// searchBar.style.display = "none";
export { gotoMain };
