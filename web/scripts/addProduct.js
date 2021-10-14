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
const pTitle = document.getElementById("add-product-page-title");
const pPrice = document.getElementById("add-product-page-price");
const pDescription = document.getElementById("add-product-page-desc");
const pisVisible = document.getElementById("product-detail-condition");
const pSize = document.getElementById("product-detail-size");
const pQty = document.getElementById("product-detail-qty");

// product img
const adminAddImgBtn = document.getElementById("upload-img");
const adminMainImg = document.getElementById("admin-main-img");
const adminAddImgList = document.getElementById("admin-add-product-img-list");
const adminAddImgTemplate = document.getElementById("add-product-img-template");

// variables
// TODO flush imgs after submit URL.revokeObjectURL(output.src)
let imgsArray = [];
let imgsArrayToFlush = [];

const gotoAddProductPage = () => {
  searchPage.style.display = "none";
  searchBar.style.display = "none";
  addProductPage.style.display = "block";
  pTitle.value = "";
  pPrice.value = "";
  pDescription.value = "";
  pSize.value = "";
  pQty.value = "";
  adminMainImg.src = "assets/shoe.png";
  // erase all img at beginning
  while (adminAddImgList.firstChild) {
    adminAddImgList.removeChild(adminAddImgList.firstChild);
  }
  imgsArray = [];
  imgsArrayToFlush = [];
};

const goBack = () => {
  searchPage.style.display = "block";
  searchBar.style.display = "block";
  addProductPage.style.display = "none";
  getProductList();
};

const addProductToDB = async () => {
  // price validation
  // quantity validation
  if (pTitle.value === "") {
    alert("Please Enter Product Title");
    return;
  }

  if (!/^\d+(\.\d{1,2})?$/.test(pPrice.value)) {
    alert("Please Enter Valid Price (Maximum 2 dicimal) e.g. 99.99");
    return;
  }
  if (!/^\d+$/.test(pQty.value)) {
    alert("Please Enter Valid Quantity");
    return;
  }

  try {
    let id = "_" + Math.random().toString(36).substr(2, 9);
    const url = `http://localhost:8080/admin/add/${id}?size=${pSize.value}&stock=${pQty.value}`;
    const data = JSON.stringify({
      name: pTitle.value,
      price: pPrice.value,
      description: pDescription.value,
      visibility: pisVisible.value == "on" ? 1 : 0,
    });
    console.log(data);
    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: data,
    });
    const jsData = await response.text();
    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    }

    // upload img API request
    const url2 =
      "https://api.imgbb.com/1/upload?expiration=600&key=ce665509ac9711eff2bf21d3a2cb1831";

    if (imgsArray.length != 0) {
      for (let img of imgsArray) {
        const formData = new FormData();
        formData.append("image", img);
        const response2 = await fetch(url2, {
          method: "POST",
          body: formData,
        });
        const jsData2 = await response2.text();
        const d2 = JSON.parse(jsData2);
        console.log("Completed!", d2);
        if (d2.status === "fail") {
          alert("Something Wrong");
        }
      }
    } else {
      console.log("No img Upload");
    }
    // no error
    alert("Added Successfully");
    goBack();
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

const showPreview = () => {
  const img = adminAddImgBtn.files[0];
  if (img) {
    adminMainImg.src = URL.createObjectURL(img);
    const newSubImg = adminAddImgTemplate.cloneNode(true);
    newSubImg.id = adminMainImg.src;
    newSubImg.addEventListener("click", () => {
      adminMainImg.src = URL.createObjectURL(img);
    });
    newSubImg.src = URL.createObjectURL(img);
    adminAddImgList.appendChild(newSubImg);
    imgsArray.push(img);
    imgsArrayToFlush.push(newSubImg.src);
  }

  console.log(imgsArray);
};

gotoAddProductBtn.addEventListener("click", gotoAddProductPage);
goBackBtn.addEventListener("click", goBack);
addProductBtn.addEventListener("click", addProductToDB);
adminAddImgBtn.addEventListener("change", showPreview);
