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

const gotoMain = () => {
  accountPage.style.display = "none";
  addProductPage.style.display = "none";
  if (sessionStorage.getItem("isAdmin") == 1) {
    adminHeader.style.display = "block";
    userHeader.style.display = "none";
    searchPage.style.display = "block";
    searchBar.style.display = "block";
    recommendPage.style.display = "none";
    userSearchPage.style.display = "none";
    userSearchBar.style.display = "none";
    userAboutPage.style.display = "none";
  } else {
    // customer
    adminHeader.style.display = "none";
    userHeader.style.display = "block";
    searchPage.style.display = "none";
    searchBar.style.display = "none";
    recommendPage.style.display = "block";
    userSearchPage.style.display = "none";
    userSearchBar.style.display = "none";
    userAboutPage.style.display = "none";
  }
};
gotoMain();
// add product detail
// searchPage.style.display = "none";
// searchBar.style.display = "none";
export { gotoMain };
