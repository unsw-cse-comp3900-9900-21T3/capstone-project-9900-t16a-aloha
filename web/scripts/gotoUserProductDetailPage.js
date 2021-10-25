import { gotoMain } from "./gotoMain.js";
import { createUId, removeAllChilds } from "./helper.js";
const backBtn = document.getElementById("user-product-page-back-btn");
backBtn.addEventListener("click", gotoMain);

const pBrand = document.getElementById("user-product-detail-brand");
const pTitle = document.getElementById("user-product-detail-name");
const pPrice = document.getElementById("user-product-detail-price");
const pSize = document.getElementById("user-product-detail-size");
const pQty = document.getElementById("user-product-detail-qty");
const pDescription = document.getElementById("user-product-page-desc");
const imgList = document.getElementById("user-product-img-list");
const imgTemplate = document.getElementById("add-product-img-template");
const editMainImg = document.getElementById("user-product-main-img");

const addCartBtn = document.getElementById("user-addcart-product-btn");
let pid;
let stockPair = new Map();
const getProductDetail = async (productId) => {
  removeAllChilds(imgList);
  pSize.value = "default";
  pQty.value = "";
  pid = productId;
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
      pTitle.innerText = d.name;

      pPrice.innerText = "$" + d.price;
      pBrand.innerText = d.brand;

      pDescription.innerText = d.description;
      console.log(d.brand);
      //  get stock API
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
        console.log(stockPair);
      } catch (err) {
        console.error(`Error: ${err}`);
      }
      // pSize.value = "";
      // pQty.value = "";
      // loop img

      if (d.imgURL != null && d.imgURL != "[]") {
        const imgsArrayJson = JSON.parse(d.imgURL);
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

["keyup"].forEach((evt) =>
  pQty.addEventListener(evt, () => {
    if (pSize.value === "default") {
      alert("Please select size first");
      pQty.value = "";
    } else {
      // check qty less than stk
      if (stockPair.get(pSize.value)) {
        if (
          parseInt(pQty.value, 10) > parseInt(stockPair.get(pSize.value), 10)
        ) {
          alert("We don't have this much stock.");
          pQty.value = "1";
        }
        if (stockPair.get(pSize.value) == 0) {
          pQty.value = "0";
        }
      } else {
        alert("We run out of stock.");
        pQty.value = "0";
      }
    }
  })
);

pSize.addEventListener("change", () => {
  //   if (stockPair.get(pSize.value)) {
  //     pQty.value = stockPair.get(pSize.value);
  //   } else {
  if (stockPair.get(pSize.value)) {
    pQty.value = "1";
  } else {
    alert("We run out of stock.");
    pQty.value = "0";
  }

  //   }
});

addCartBtn.addEventListener("click", async () => {
  const userId = sessionStorage.getItem("userID");
  const url = `http://localhost:8080/test/user/shoppingcart/add?userid=${userId}&productid=${pid}&size=${pSize.value}&quantity=${pQty.value}`;
  try {
    await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
    });
    if (!pQty.value) {
      alert("Please enter valid qty");
      return;
    }
    if (pQty.value == 0) {
      alert("We run out of stock.");
      return;
    }

    alert("Added to cart Successfully!");
  } catch (err) {
    console.log(err);
  }
});

export { getProductDetail };
