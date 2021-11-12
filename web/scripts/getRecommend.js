import { removeAllChilds } from "./helper.js";
import { gotoUserProductDetailPageDom } from "./main.js";
import { getAllWishList } from "./getAllWishList.js";

const productTemplate = document.getElementById("product-temp");
const productList = document.getElementById("recommend-page-products");

const getRecommend = async () => {
  removeAllChilds(productList);
  try {
    const url = `http://localhost:8080/test/getRecommend/${sessionStorage.getItem(
      "userID"
    )}`;

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
      for (let p in d) {
        if (productList.childNodes.length === 8) {
          break;
        }
        if (p == "s9" || p == "s10") {
          continue;
        }
        if (p != "id") {
          try {
            const url = `http://localhost:8080/admin/search/id/${d[p]}`;
            const response = await fetch(url, {
              method: "get",
              headers: {
                "content-type": "application/json",
              },
            });
            const jsData = await response.text();
            const e = JSON.parse(jsData);

            // const isExistP = document.getElementById(e.id);

            if (true) {
              const productTempNew = productTemplate.cloneNode(true);

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

              // wishlist  heart condition
              const inWishList = getAllWishList();
              inWishList.then((items) => {
                items.forEach((p) => {
                  if (productTempNew.id == p.productID) {
                    productTempNew
                      .getElementsByClassName("toggle-wishlist")[0]
                      .classList.remove("bi-heart");
                    productTempNew
                      .getElementsByClassName("toggle-wishlist")[0]
                      .classList.add("bi-heart-fill");
                  }
                });
              });

              // goto detail pages
              productTempNew.addEventListener("click", () => {
                gotoUserProductDetailPageDom(productTempNew.id);
              });
              productList.appendChild(productTempNew);
            }
          } catch (err) {
            console.log(err);
          }
        }
      }

      addWishListListener();
    }
  } catch (err) {
    console.log(err);
  }
};

const togWishList = async (e) => {
  e.stopPropagation();
  const userId = sessionStorage.getItem("userID");
  const pId = e.currentTarget.parentNode.parentNode.parentNode.parentNode.id;

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
const addWishListListener = () => {
  const wishHeart = document.getElementsByClassName("toggle-wishlist");
  for (let ele of wishHeart) {
    ele.removeEventListener("click", togWishList);
    ele.addEventListener("click", togWishList);
  }
};

export { getRecommend };
