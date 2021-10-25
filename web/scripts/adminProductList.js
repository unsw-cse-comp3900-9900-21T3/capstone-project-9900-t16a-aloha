import { gotoEditProductPage } from "./editProduct.js";
import { removeAllChilds } from "./helper.js";
const productTemp = document.getElementById("admin-product-list-temp");
const productList = document.getElementById("admin-product-list-parent");

const loadMoreBtn = document.getElementById("admin-load-more");

const selectBrand = document.getElementById("admin-search-brand");
const selectPrice = document.getElementById("admin-search-price");
const searchBtn = document.getElementById("admin-search-btn");

//TODO  getProductList has option of orders
const getProductList = async () => {
  removeAllChilds(productList);
  offset = 1;
  loadMoreBtn.style.display = "initial";
  try {
    const url = "http://localhost:8080/admin/showall";

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
      const items = d.content;
      items.forEach(
        (e) => {
          const isExistP = document.getElementById(e.id);

          if (e.isDeleted === 0 && !isExistP) {
            const productTempNew = productTemp.cloneNode(true);

            // set delete button
            productTempNew.getElementsByClassName(
              "product-list-page-card-delete"
            )[0].id = "del-btn-" + e.id;

            productTempNew.id = e.id;

            productTempNew.getElementsByClassName("card-text")[0].innerText =
              e.name;
            productTempNew.getElementsByClassName("badge")[0].innerText =
              "$" + e.price;

            const imgUrlArrays = JSON.parse(e.imgURL);
            // console.log(imgUrlArrays);
            if (e.imgURL == "[]" || !e.imgURL) {
              productTempNew.getElementsByClassName("card-img-top")[0].src =
                "assets/shoe.png";
            } else {
              productTempNew.getElementsByClassName("card-img-top")[0].src =
                imgUrlArrays[0];
            }
            // goto edit pages
            productTempNew.addEventListener("click", () => {
              gotoEditProductPage(e.id);
            });
            productList.appendChild(productTempNew);
          }
        }
        // prevent delete event propagation
      );
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};

//  load more
// todo load more with filter
const loadMoreProductList = async (offset) => {
  try {
    const url = `http://localhost:8080/admin/showall?pageindex=${offset}`;
    console.log(url);
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
      // console.log(d);
      const items = d.content;
      if (items.length == 0) {
        loadMoreBtn.style.display = "none";
        return;
      }
      items.forEach(
        (e) => {
          const isExistP = document.getElementById(e.id);

          if (e.isDeleted === 0 && !isExistP) {
            const productTempNew = productTemp.cloneNode(true);

            // set delete button
            productTempNew.getElementsByClassName(
              "product-list-page-card-delete"
            )[0].id = "del-btn-" + e.id;

            productTempNew.id = e.id;

            productTempNew.getElementsByClassName("card-text")[0].innerText =
              e.name;
            productTempNew.getElementsByClassName("badge")[0].innerText =
              "$" + e.price;

            const imgUrlArrays = JSON.parse(e.imgURL);
            // console.log(imgUrlArrays);
            if (e.imgURL == "[]" || !e.imgURL) {
              productTempNew.getElementsByClassName("card-img-top")[0].src =
                "assets/shoe.png";
            } else {
              productTempNew.getElementsByClassName("card-img-top")[0].src =
                imgUrlArrays[0];
            }
            // goto edit pages
            productTempNew.addEventListener("click", () => {
              gotoEditProductPage(e.id);
            });
            productList.appendChild(productTempNew);
          }
        }
        // prevent delete event propagation
      );
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};
let offset = 1;
loadMoreBtn.addEventListener("click", () => {
  loadMoreProductList(offset);
  offset++;
});

export { getProductList };
