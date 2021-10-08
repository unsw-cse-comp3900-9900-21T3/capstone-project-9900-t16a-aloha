// admin/user header
const adminHeader = document.getElementById("admin-header");
const userHeader = document.getElementById("user-header");

// pages DOM
const accountPage = document.getElementById("account-page");
const searchPage = document.getElementById("product-search-page");
const searchBar = document.getElementById("search-bar");
const addProductPage = document.getElementById("add-product-detail-page");

const gotoMain = () => {
  accountPage.style.display = "none";
  addProductPage.style.display = "none";
  if (sessionStorage.getItem("isAdmin") == 1) {
    adminHeader.style.display = "block";
    userHeader.style.display = "none";
    searchPage.style.display = "block";
    searchBar.style.display = "block";
  } else {
    // customer
    adminHeader.style.display = "none";
    userHeader.style.display = "block";
    searchPage.style.display = "none";
    searchBar.style.display = "none";
  }
};
gotoMain();
// add product detail
// searchPage.style.display = "none";
// searchBar.style.display = "none";
export { gotoMain };
