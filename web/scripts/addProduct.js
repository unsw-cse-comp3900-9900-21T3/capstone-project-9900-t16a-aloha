import { getProductList } from "./adminProductList.js";

// header bar button
const gotoAddProductBtn = document.getElementById("admin-add-shoe-btn");

// page DOM
const searchPage = document.getElementById("product-search-page");
const addProductPage = document.getElementById("add-product-detail-page");
const searchBar = document.getElementById("search-bar");

// add product page buttons
const goBackBtn = document.getElementById("add-product-page-back-btn");
const addProductBtn = document.getElementById("admin-add-product-btn");

//add product page input fields
// const pTitle = document.getElementById("add-product-page-title");
// const pPrice = document.getElementById("add-product-page-price");
const pDescription = document.getElementById("add-product-page-desc");
const pisVisible = document.getElementById("product-detail-condition");

const gotoAddProductPage = () => {
  searchPage.style.display = "none";
  searchBar.style.display = "none";
  addProductPage.style.display = "block";
};

const goBack = () => {
  searchPage.style.display = "block";
  searchBar.style.display = "block";
  addProductPage.style.display = "none";
  getProductList();
};

const addProductToDB = async () => {
  try {
    let id = "_" + Math.random().toString(36).substr(2, 9);
    const url = `http://localhost:8080/admin/add/${id}`;
    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({}),
    });
    const jsData = await response.text();
    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      alert("Added Successfully");
      goBack();
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

gotoAddProductBtn.addEventListener("click", gotoAddProductPage);
goBackBtn.addEventListener("click", goBack);
addProductBtn.addEventListener("click", addProductToDB);
