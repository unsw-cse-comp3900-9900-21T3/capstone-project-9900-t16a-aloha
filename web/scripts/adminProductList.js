import { gotoEditProductPage } from "./editProduct.js";
import { removeAllChilds } from "./helper.js";
const productTemp = document.getElementById("admin-product-list-temp");
const productList = document.getElementById("admin-product-list-parent");

const loadMoreBtn = document.getElementById("admin-load-more");

const selectBrand = document.getElementById("admin-search-brand");
const selectPrice = document.getElementById("admin-search-price");
const searchBtn = document.getElementById("admin-search-btn");

const sortBy = document.getElementById("admin-search-sortby");
const searchInput = document.getElementById("admin-search-input");

const getProductList = async () => {
  removeAllChilds(productList);
  offset = 1;
  filterIndex = 0;
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
let filterIndex = 0;
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
  let url = `http://localhost:8080/admin/search/?pageindex=${filterIndex}`;
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

        if (e.isDeleted === 0 && !isExistP) {
          const productTempNew = productTemp.cloneNode(true);

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
      });
    }
  } catch (err) {
    console.log(err);
  }
};

const priceBrandLoadMore = async (filterIndex) => {
  let url = `http://localhost:8080/admin/search/?pageindex=${filterIndex}`;
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
          const productTempNew = productTemp.cloneNode(true);

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

          // goto detail pages
          productTempNew.addEventListener("click", () => {
            gotoEditProductPage(productTempNew.id);
          });
          productList.appendChild(productTempNew);
        }
      });
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

export { getProductList };
