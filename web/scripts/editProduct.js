import { getProductList } from "./adminProductList.js";
import { fileToDataUrl, createUId, removeAllChilds } from "./helper.js";
import showModal from "./errmodal.js";
// page DOM
const searchPage = document.getElementById("product-search-page");
const editProductPage = document.getElementById("edit-product-detail-page");
const searchBar = document.getElementById("search-bar");

const pBrand = document.getElementById("edit-product-page-brand");
const pTitle = document.getElementById("edit-product-page-title");
const pPrice = document.getElementById("edit-product-page-price");
const pSize = document.getElementById("edit-product-detail-size");
const pQty = document.getElementById("edit-product-detail-qty");
const pDescription = document.getElementById("edit-product-page-desc");
const isVisible = document.getElementById("edit-product-detail-condition");
const imgList = document.getElementById("admin-edit-product-img-list");
const imgTemplate = document.getElementById("add-product-img-template");
const editMainImg = document.getElementById("admin-edit-main-img");
const backbtn = document.getElementById("edit-product-page-back-btn");
const changeBtn = document.getElementById("admin-edit-product-btn");

const adminAddImgTemplate = document.getElementById("add-product-img-template");

const delImgBtn = document.getElementById("edit-del-product-img");

const uploadImg = document.getElementById("edit-upload-img");
//* all imgs including new added
let imgsArray = [];
let stockPair = new Map();
const getProductById = async (productId) => {
  pSize.value = "default";
  pQty.value = "";
  imgsArray = [];
  stockPair.clear();
  try {
    const url = `http://localhost:8080/admin/search/id/${productId}`;
    const response = await fetch(url, {
      method: "get",
      headers: {
        "content-type": "application/json",
      },
    });

    const jsData = await response.text();

    const d = JSON.parse(jsData);
    console.log("Completed search Id!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      pTitle.value = d.name;
      pPrice.value = d.price;
      pBrand.value = d.brand;
      pDescription.value = d.description;
      isVisible.checked = d.visibility == 1 ? true : false;

      try {
        const url = "http://localhost:8080/admin/showallstorge";
        const response = await fetch(url, {
          method: "get",
          headers: {
            "content-type": "application/json",
          },
        });

        const jsData = await response.text();

        const d = JSON.parse(jsData);
        d.forEach((stk) => {
          // console.log(stk);
          // console.log(stk.storgeid.product.id);
          // console.log(stk.stock);
          // console.log(stk.storgeid.size);
          if (stk.storgeid.product.id === productId) {
            stockPair.set(stk.storgeid.size.toString(), stk.stock.toString());
          }
        });
      } catch (err) {
        console.error(`Error: ${err}`);
      }
      // pSize.value = "";
      // pQty.value = "";
      // loop img

      if (d.imgURL != null && d.imgURL != "[]") {
        const imgsArrayJson = JSON.parse(d.imgURL);
        imgsArray = imgsArrayJson;
        // imgsArray.push(d.imgURL);
        imgsArrayJson.forEach((i) => {
          const subImgContainer = imgTemplate.cloneNode(true);
          const subImg = subImgContainer.getElementsByTagName("img")[0];
          subImgContainer.id = createUId("subImg");
          subImg.src = i;
          subImg.addEventListener("click", () => {
            editMainImg.src = i;
          });
          imgList.appendChild(subImgContainer);
        });
        editMainImg.src = imgsArrayJson[0];
      } else {
        editMainImg.src = "assets/shoe.png";
      }
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

const gotoProductList = () => {
  searchPage.style.display = "block";
  searchBar.style.display = "block";
  editProductPage.style.display = "none";
  getProductList();
};

const changeProductPost = async () => {
  // price validation
  // quantity validation
  if (pTitle.value === "") {
    showModal("Please Enter Product Title");
    // alert("Please Enter Product Title");
    return;
  }

  if (!/^\d+(\.\d{1,2})?$/.test(pPrice.value)) {
    showModal("Please Enter Valid Price (Maximum 2 dicimal) e.g. 99.99");
    // alert("Please Enter Valid Price (Maximum 2 dicimal) e.g. 99.99");
    return;
  }

  console.log(pSize.value);
  if (pSize.value !== "default") {
    if (!/^\d+$/.test(pQty.value)) {
      showModal("Please Enter Valid Quantity");
      // alert("Please Enter Valid Quantity");
      return;
    }
  }

  const url = `http://localhost:8080/admin/update/?id=${pId}&name=${encodeURIComponent(
    pTitle.value
  )}&price=${pPrice.value}&brand=${encodeURIComponent(
    pBrand.value
  )}&desc=${encodeURIComponent(pDescription.value)}&visibility=${
    isVisible.checked ? 1 : 0
  }`;
  console.log(isVisible);
  console.log(isVisible.checked);
  console.log(url);
  try {
    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({ imgurls: JSON.stringify(imgsArray) }),
    });

    const jsData = await response.text();

    const d = JSON.parse(jsData);
    console.log("Completed search Id!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      if (stockPair.size != 0) {
        for (let shoeSize of stockPair.keys()) {
          console.log("key:" + shoeSize + ", value:" + stockPair.get(shoeSize));
          let num = stockPair.get(shoeSize);
          if (num === "") {
            continue;
          }
          let url = `http://localhost:8080/admin/add?id=${pId}&size=${shoeSize}&stock=${num}`;
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
      showModal("Edit successfully");
      // alert("Edit successfully");
      gotoProductList();
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

backbtn.addEventListener("click", gotoProductList);

changeBtn.addEventListener("click", changeProductPost);
let pId;
export const gotoEditProductPage = (productId) => {
  searchPage.style.display = "none";
  searchBar.style.display = "none";
  editProductPage.style.display = "block";
  // erase all img at beginning
  removeAllChilds(imgList);
  getProductById(productId);
  pId = productId;
};

const addPreview = async () => {
  console.log(imgsArray);
  if (imgList.childNodes.length === 8) {
    showModal("No more than 8 imgs");
    // alert("No more than 8 imgs");
    return;
  }
  const img = uploadImg.files[0];

  if (img) {
    const imgUrl = await fileToDataUrl(img);
    editMainImg.src = imgUrl;

    const subImgContainer = adminAddImgTemplate.cloneNode(true);
    const newSubImg = subImgContainer.getElementsByTagName("img")[0];
    subImgContainer.id = "subImg" + Math.random().toString(36).substr(2, 9);
    newSubImg.src = imgUrl;
    newSubImg.addEventListener("click", () => {
      editMainImg.src = imgUrl;
    });

    imgList.appendChild(subImgContainer);
    imgsArray.push(imgUrl);
  }
};

uploadImg.addEventListener("change", addPreview);
delImgBtn.addEventListener("click", () => {
  if (imgList.childNodes.length === 0) {
    showModal("No img to delete");
    // alert("No img to delete");
    return;
  }

  const srcDel = editMainImg.src;
  // 1. delete sub img
  for (let subImg of imgList.childNodes) {
    const imgDel = subImg.getElementsByTagName("img")[0];
    if (imgDel.src === editMainImg.src) {
      subImg.remove();
      break;
    }
  }
  // 2. change main img src
  if (imgList.childNodes.length > 0) {
    const firstImg = imgList.childNodes[0].getElementsByTagName("img")[0];
    editMainImg.src = firstImg.src;
  } else {
    editMainImg.src = "assets/shoe.png";
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
      showModal("Please select size first");
      // alert("Please select size first");
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
