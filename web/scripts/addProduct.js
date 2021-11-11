import { getProductList } from "./adminProductList.js";
import { fileToDataUrl } from "./helper.js";
// header bar button
const gotoAddProductBtn = document.getElementById("admin-add-shoe-btn");

// page DOM
const searchPage = document.getElementById("product-search-page");
const addProductPage = document.getElementById("add-product-detail-page");
const searchBar = document.getElementById("search-bar");

// edit page
const editProductsPage = document.getElementById("edit-product-detail-page");

// add product page buttons
const goBackBtn = document.getElementById("add-product-page-back-btn");
const addProductBtn = document.getElementById("admin-add-product-btn");

//add product page input fields
const pTitle = document.getElementById("add-product-page-title");
const pPrice = document.getElementById("add-product-page-price");
const pDescription = document.getElementById("add-product-page-desc");
const pisVisible = document.getElementById("product-detail-condition");
const pSize = document.getElementById("add-product-detail-size");
const pQty = document.getElementById("add-product-detail-qty");
const pBrand = document.getElementById("add-product-page-brand");
// product img
const adminAddImgBtn = document.getElementById("upload-img");
const adminMainImg = document.getElementById("admin-main-img");
const adminAddImgList = document.getElementById("admin-add-product-img-list");
const adminAddImgTemplate = document.getElementById("add-product-img-template");

const addImgDelBtn = document.getElementById("add-del-product-img");

// variables

let imgsArray = [];
let stockPair = new Map();
const gotoAddProductPage = () => {
  stockPair.clear();
  searchPage.style.display = "none";
  searchBar.style.display = "none";
  editProductsPage.style.display = "none";
  addProductPage.style.display = "block";
  pTitle.value = "";
  pPrice.value = "";
  pBrand.value = "";
  pDescription.value = "";
  pSize.value = "default";
  pQty.value = "";
  adminMainImg.src = "assets/shoe.png";
  // erase all img at beginning
  while (adminAddImgList.firstChild) {
    adminAddImgList.removeChild(adminAddImgList.firstChild);
  }
  imgsArray = [];
};

const goBack = () => {
  searchPage.style.display = "block";
  searchBar.style.display = "block";
  addProductPage.style.display = "none";
  editProductsPage.style.display = "none";
  getProductList();
};

const addProductToDB = async () => {
  // price validation
  // quantity validation
  if (pBrand.value === "") {
    alert("Please Enter Product Brand");
    return;
  }
  if (pTitle.value === "") {
    alert("Please Enter Product Title");
    return;
  }

  if (!/^\d+(\.\d{1,2})?$/.test(pPrice.value)) {
    alert("Please Enter Valid Price (Maximum 2 dicimal) e.g. 99.99");
    return;
  }

  if (pSize.value !== "default") {
    if (!/^\d+$/.test(pQty.value)) {
      alert("Please Enter Valid Quantity");
      return;
    }
  }
  try {
    let id = "_" + Math.random().toString(36).substr(2, 9);

    const url = `http://localhost:8080/admin/add/${id}?visibility=${
      pisVisible.checked ? 1 : 0
    }`;
    let data = {
      name: pTitle.value,
      price: pPrice.value,
      brand: pBrand.value,
      description: pDescription.value,
    };

    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify(data),
    });
    const jsData = await response.text();
    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    }

    //  upload img API request
    if (imgsArray.length != 0) {
      const url = `http://localhost:8080/admin/update?id=${id}`;
      const imgsArrayString = JSON.stringify(imgsArray);
      console.log(imgsArrayString);
      const response = await fetch(url, {
        method: "post",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          imgurls: imgsArrayString,
        }),
      });
      const jsData = await response.text();
      const d = JSON.parse(jsData);
      console.log("Completed!", d);
      if (d.status === "fail") {
        alert("Something Wrong");
      }
    } else {
      console.log("No img Upload");
    }
    console.log(stockPair.size);
    if (stockPair.size != 0) {
      for (let shoeSize of stockPair.keys()) {
        console.log("key:" + shoeSize + ", value:" + stockPair.get(shoeSize));
        let num = stockPair.get(shoeSize);
        if (num === "") {
          continue;
        }
        let url = `http://localhost:8080/admin/add?id=${id}&size=${shoeSize}&stock=${num}`;
        console.log(url);
        let response = await fetch(url, {
          method: "post",
          headers: {
            "content-type": "application/json",
          },
        });
        let jsData = await response.text();
        let d = JSON.parse(jsData);
        // console.log("Completed!", d);
        if (d.status === "fail") {
          alert("Something Wrong in stock");
        }
      }
    }
    // no error
    alert("Added Successfully");
    goBack();
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

const showPreview = async () => {
  console.log(imgsArray.length);
  if (imgsArray.length >= 8) {
    alert("No more than 8 imgs");
    return;
  }
  const img = adminAddImgBtn.files[0];

  if (img) {
    const imgUrl = await fileToDataUrl(img);
    adminMainImg.src = imgUrl;
    // adminMainImg.src = URL.createObjectURL(img);

    const subImgContainer = adminAddImgTemplate.cloneNode(true);
    const newSubImg = subImgContainer.getElementsByTagName("img")[0];
    subImgContainer.id = "subImg" + Math.random().toString(36).substr(2, 9);
    newSubImg.src = imgUrl;
    newSubImg.addEventListener("click", () => {
      // adminMainImg.src = URL.createObjectURL(img);
      adminMainImg.src = imgUrl;
    });

    // newSubImg.src = URL.createObjectURL(img);
    adminAddImgList.appendChild(subImgContainer);
    imgsArray.push(imgUrl);
  }
};

gotoAddProductBtn.addEventListener("click", gotoAddProductPage);
goBackBtn.addEventListener("click", goBack);
addProductBtn.addEventListener("click", addProductToDB);
adminAddImgBtn.addEventListener("change", showPreview);

addImgDelBtn.addEventListener("click", () => {
  if (adminAddImgList.childNodes.length === 0) {
    alert("No img to delete");
    return;
  }
  const srcDel = adminMainImg.src;
  // 1. delete sub img
  for (let subImg of adminAddImgList.childNodes) {
    const imgDel = subImg.getElementsByTagName("img")[0];
    if (imgDel.src === adminMainImg.src) {
      subImg.remove();
    }
  }
  // 2. change main img src
  if (adminAddImgList.childNodes.length > 0) {
    const firstImg =
      adminAddImgList.childNodes[0].getElementsByTagName("img")[0];
    adminMainImg.src = firstImg.src;
  } else {
    adminMainImg.src = "assets/shoe.png";
  }
  // 3. del ele in imgsArray
  const index = imgsArray.indexOf(srcDel);
  if (index > -1) {
    imgsArray.splice(index, 1);
  }
});

["keyup", "change"].forEach((evt) =>
  pQty.addEventListener(evt, () => {
    if (pSize.value === "default") {
      alert("Please select size first");
      pQty.value = "";
    } else {
      stockPair.set(pSize.value, pQty.value);
    }
  })
);
pSize.addEventListener("change", () => {
  if (stockPair.get(pSize.value)) {
    pQty.value = stockPair.get(pSize.value);
  } else {
    pQty.value = "";
  }
});
