const productTemp = document.getElementById("admin-product-list-temp");
const productList = document.getElementById("admin-product-list-parent");

const getProductList = async () => {
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
      d.forEach((e) => {
        const isExistP = document.getElementById(e.id);
        if (!isExistP) {
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
          productTempNew.getElementsByClassName("card-img-top")[0].src =
            e.imgURL;

          productList.appendChild(productTempNew);
        }
      });
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
};
export { getProductList };
getProductList();
