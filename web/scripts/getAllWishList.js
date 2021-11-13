import { gotoUserProductDetailPageDom } from "./main.js";
import { removeAllChilds } from "./helper.js";
const wishCanvas = document.getElementById("wishlist");

const productTemplate = document.getElementById("product-temp");
const wishListParent = document.getElementById("wish-list-parent");
const showWishList = async () => {
  removeAllChilds(wishListParent);
  const llist = getAllWishList();
  llist.then((items) => {
    items.forEach(async (w) => {
      const pId = w.productID;
      // get product detail
      const url = `http://localhost:8080/admin/search/id/${pId}`;
      const response = await fetch(url, {
        method: "get",
        headers: {
          "content-type": "application/json",
        },
      });
      const jsData = await response.text();
      const productDetail = JSON.parse(jsData);
      const nameOfP = productDetail.name;
      const priceOfP = productDetail.price;
      let imgSrc;
      if (productDetail.imgURL != null && productDetail.imgURL != "[]") {
        const imgsArrayJson = JSON.parse(productDetail.imgURL);
        imgSrc = imgsArrayJson[0];
      } else {
        imgSrc = "assets/shoe.png";
      }

      const productTempNew = productTemplate.cloneNode(true);

      productTempNew.id = pId + "-Wish";

      productTempNew.getElementsByClassName("card-text")[0].innerText = nameOfP;
      productTempNew.getElementsByClassName("badge")[0].innerText =
        "$" + priceOfP;

      productTempNew.getElementsByClassName("card-img-top")[0].src = imgSrc;

      // wishlist  heart condition
      productTempNew
        .getElementsByClassName("toggle-wishlist")[0]
        .classList.remove("bi-heart");
      productTempNew
        .getElementsByClassName("toggle-wishlist")[0]
        .classList.add("bi-heart-fill");
      productTempNew
        .getElementsByClassName("toggle-wishlist")[0]
        .addEventListener("click", togWishList);

      // goto detail pages
      productTempNew.addEventListener("click", () => {
        gotoUserProductDetailPageDom(pId);
      });
      wishListParent.appendChild(productTempNew);
    });
  });
};
const getAllWishList = async () => {
  const userId = sessionStorage.getItem("userID");
  const url = `http://localhost:8080/test/user/wishlist/show?userid=${userId}`;
  const response = await fetch(url, {
    method: "get",
    headers: {
      "content-type": "application/json",
    },
  });
  const jsData = await response.text();
  const d = JSON.parse(jsData);

  if (d.status === "fail") {
    alert("Something Wrong");
  } else {
    return d;
  }
};

const addWishListListener = () => {
  const wishHeart = document.getElementsByClassName("toggle-wishlist");
  console.log(wishHeart.length);
  for (let ele of wishHeart) {
    ele.removeEventListener("click", togWishList);
    ele.addEventListener("click", togWishList);
  }
};

const togWishList = async (e) => {
  e.stopPropagation();
  const userId = sessionStorage.getItem("userID");
  let pId = e.currentTarget.parentNode.parentNode.parentNode.parentNode.id;

  pId = pId.substring(0, 10);

  try {
    //  del wish api request
    if (e.currentTarget.classList.contains("bi-heart-fill")) {
      e.currentTarget.classList.remove("bi-heart-fill");
      e.currentTarget.classList.add("bi-heart");

      let url = `http://localhost:8080/test/user/wishlist/remove?userid=${userId}&productid=${pId}&size=0`;
      await fetch(url, {
        method: "post",
        headers: {
          "content-type": "application/json",
        },
      });
    }

    //  add wish api request
    else if (e.currentTarget.classList.contains("bi-heart")) {
      e.currentTarget.classList.add("bi-heart-fill");
      e.currentTarget.classList.remove("bi-heart");
      let url = `http://localhost:8080/test/user/wishlist/add?userid=${userId}&productid=${pId}&size=0`;
      await fetch(url, {
        method: "post",
        headers: {
          "content-type": "application/json",
        },
      });
    }
  } catch (err) {
    console.log(err);
  }
};

wishCanvas.addEventListener("show.bs.offcanvas", showWishList);
export { getAllWishList };
