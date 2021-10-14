import { getProductList } from "./adminProductList.js";

// page DOM
const searchPage = document.getElementById("product-search-page");
const editProductPage = document.getElementById("edit-product-detail-page");
const searchBar = document.getElementById("search-bar");

const pTitle = document.getElementById("edit-product-page-title");
const pPrice = document.getElementById("edit-product-page-price");
const pDescription = document.getElementById("edit-product-page-desc");
const isVisible = document.getElementById("edit-product-detail-condition");
const imgList = document.getElementById("admin-edit-product-img-list");
const imgTemplate = document.getElementById("add-product-img-template");
const editMainImg = document.getElementById("admin-edit-main-img");

const getProductById = async (productId) => {
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
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      pTitle.value = d.name;
      pPrice.value = d.price;
      pDescription.value = d.description;

      // TODO  get stock API
      // pSize.value = "";
      // pQty.value = "";
      // loop img
      const subImg = imgTemplate.cloneNode(true);
      subImg.id = d.imgURL;
      subImg.src = d.imgURL;
      isVisible.checked = d.visibility == 1 ? true : false;

      if (d.imgURL) {
        subImg.addEventListener("click", () => {
          editMainImg.src = d.imgURL;
        });
        imgList.appendChild(subImg);

        editMainImg.src = d.imgURL;
      } else {
        editMainImg.src = "assets/shoe.png";
      }
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

//TODO get product info while goto edit
export const gotoEditProductPage = (productId) => {
  searchPage.style.display = "none";
  searchBar.style.display = "none";
  editProductPage.style.display = "block";
  // erase all img at beginning
  while (imgList.firstChild) {
    imgList.removeChild(imgList.firstChild);
  }
  getProductById(productId);
};
