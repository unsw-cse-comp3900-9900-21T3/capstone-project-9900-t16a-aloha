import { removeAllChilds } from "./helper.js";
import { gotoUserProductDetailPageDom } from "./main.js";
import { getAllWishList } from "./getAllWishList.js";
const productTemplate = document.getElementById("product-temp");
const productList = document.getElementById("user-search-page-products");

// const addCartBtn = document.getElementById("user-addcart-product-btn");
const loadMoreBtn = document.getElementById("search-load-more-btn");

const selectBrand = document.getElementById("user-search-brand");
const selectPrice = document.getElementById("user-search-price");
const sortBy = document.getElementById("user-search-sortby");
const searchInput = document.getElementById("user-search-input");
const searchBtn = document.getElementById("user-search-btn");
let offset = 1;
let filterIndex = 0;
const getUserSearchProductList = async () => {
  removeAllChilds(productList);
  offset = 1;
  filterIndex = 0;
  loadMoreBtn.style.display = "initial";
  try {
    const url = "http://localhost:8080/test/user/product";

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
      items.forEach((e) => {
        console.log(e);
        // const isExistP = document.getElementById(e.id);

        if (true) {
          const productTempNew = productTemplate.cloneNode(true);

          // // set delete button
          //   console.log(productTempNew.getElementsByTagName("button"));
          //   productTempNew.getElementsByClassName(
          //     "product-list-page-card-wish"
          //   )[0].id = "wish-btn-" + e.id;

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
      });
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

const loadMoreUserSearch = async (offset) => {
  try {
    const url = `http://localhost:8080/test/user/product?pageindex=${offset}`;

    const response = await fetch(url, {
      method: "get",
      headers: {
        "content-type": "application/json",
      },
    });
    const jsData = await response.text();
    const d = JSON.parse(jsData);

    // console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      const items = d.content;
      if (items.length == 0) {
        loadMoreBtn.style.display = "none";
        return;
      }

      items.forEach((e) => {
        // const isExistP = document.getElementById(e.id);

        if (true) {
          const productTempNew = productTemplate.cloneNode(true);

          // // set delete button
          //   console.log(productTempNew.getElementsByTagName("button"));
          //   productTempNew.getElementsByClassName(
          //     "product-list-page-card-wish"
          //   )[0].id = "wish-btn-" + e.id;

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
      });
      addWishListListener();
    }
  } catch (err) {
    console.log(err);
  }
};
loadMoreBtn.addEventListener("click", () => {
  if (
    selectBrand.value != "" ||
    selectPrice.value != "" ||
    sortBy.value != "" ||
    searchInput.value != ""
  ) {
    filterIndex++;
    priceBrandLoadMore(filterIndex);
  } else {
    loadMoreUserSearch(offset);
    offset++;
  }
});

const priceBrand = async (filterIndex) => {
  removeAllChilds(productList);
  let url = `http://localhost:8080/test/user/search/?pageindex=${filterIndex}`;
  if (selectBrand.value != "") {
    url += "&brand=" + selectBrand.value;
  }
  if (selectPrice.value == 1) {
    url += "&maxprice=" + "100";
  } else if (selectPrice.value == 2) {
    url += "&minprice=" + "101";
    url += "&maxprice=" + "200";
  } else if (selectPrice.value == 3) {
    url += "&minprice=" + "200";
  }
  if (sortBy.value == "nameAsc") {
    url += "&sortby=name" + "&order=asc";
  } else if (sortBy.value == "nameDesc") {
    url += "&sortby=name" + "&order=desc";
  } else if (sortBy.value == "priceAsc") {
    url += "&sortby=price" + "&order=asc";
  } else if (sortBy.value == "priceDesc") {
    url += "&sortby=price" + "&order=desc";
  }

  if (searchInput.value != "") {
    url += "&name=" + searchInput.value;
  }

  console.log(url);
  try {
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
      items.forEach((e) => {
        const isExistP = document.getElementById(e.id);

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
      });
      addWishListListener();
    }
  } catch (err) {
    console.log(err);
  }
};

const priceBrandLoadMore = async (filterIndex) => {
  let url = `http://localhost:8080/test/user/search/?pageindex=${filterIndex}`;
  if (selectBrand.value != "") {
    url += "&brand=" + selectBrand.value;
  }
  if (selectPrice.value == 1) {
    url += "&maxprice=" + "100";
  } else if (selectPrice.value == 2) {
    url += "&minprice=" + "101";
    url += "&maxprice=" + "200";
  } else if (selectPrice.value == 3) {
    url += "&minprice=" + "200";
  }

  if (sortBy.value == "nameAsc") {
    url += "&sortby=name" + "&order=asc";
  } else if (sortBy.value == "nameDesc") {
    url += "&sortby=name" + "&order=desc";
  } else if (sortBy.value == "priceAsc") {
    url += "&sortby=price" + "&order=asc";
  } else if (sortBy.value == "priceDesc") {
    url += "&sortby=price" + "&order=desc";
  }

  if (searchInput.value != "") {
    url += "&name=" + searchInput.value;
  }
  console.log(url);
  try {
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
      if (items.length == 0) {
        loadMoreBtn.style.display = "none";
        return;
      }

      items.forEach((e) => {
        const isExistP = document.getElementById(e.id);

        if (e.isDeleted === 0 && !isExistP) {
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
      });
      addWishListListener();
    }
  } catch (err) {
    console.log(err);
  }
};

selectBrand.addEventListener("change", () => {
  offset = 1;
  loadMoreBtn.style.display = "initial";
  filterIndex = 0;
  priceBrand(filterIndex);
});
selectPrice.addEventListener("change", () => {
  loadMoreBtn.style.display = "initial";
  offset = 1;
  filterIndex = 0;
  priceBrand(filterIndex);
});

sortBy.addEventListener("change", () => {
  loadMoreBtn.style.display = "initial";
  offset = 1;
  filterIndex = 0;
  priceBrand(filterIndex);
});
searchBtn.addEventListener("click", () => {
  loadMoreBtn.style.display = "initial";
  offset = 1;
  filterIndex = 0;
  priceBrand(filterIndex);
});

export { getUserSearchProductList };
