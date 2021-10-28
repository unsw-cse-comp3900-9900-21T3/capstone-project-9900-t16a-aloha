import { removeAllChilds } from "./helper.js";

const shopCanvas = document.getElementById("shopping-cart");

const shoppingCartItemTemp = document.getElementById("shopping-cart-item");
const shoppingcartParent = document.getElementById("shopping-cart-list");
const checkoutAddr = document.getElementById("checkout-address");
const checkoutBtn = document.getElementById("check-out-btn");

const totalAmt = document.getElementById("total-amount-check");

const getAllShoppingCart = async () => {
  removeAllChilds(shoppingcartParent);
  shoppingcartParent.style.display = "block";
  checkoutAddr.style.display = "none";
  checkoutBtn.style.display = "block";
  const paypalBtn = document.getElementById("paypal-button-container");
  paypalBtn.innerHTML = "";

  console.log("im in");
  const userId = sessionStorage.getItem("userID");
  const url = `http://localhost:8080/test/user/shoppingcart/show?userid=${userId}`;
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
      let tAmt = 0;
      for (let p of d) {
        // get product detail
        const url = `http://localhost:8080/admin/search/id/${p.productID}`;
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

        const newItem = shoppingCartItemTemp.cloneNode(true);
        newItem.id = p.productID + "-" + p.size + "-" + p.quantity;
        newItem.getElementsByTagName("img")[0].src = imgSrc;
        newItem.getElementsByClassName("shop-name")[0].innerText = nameOfP;
        newItem.getElementsByClassName("shop-price")[0].innerText =
          "$" + priceOfP;
        tAmt += priceOfP;
        newItem.getElementsByClassName("shop-qty")[0].innerText = p.quantity;
        newItem.getElementsByClassName("shop-size")[0].innerText =
          "US " + p.size;

        newItem.getElementsByClassName("total-price")[0].innerText =
          "$" + parseFloat(priceOfP) * parseInt(p.quantity, 10);
        newItem
          .getElementsByClassName("del-shopping-cart-item")[0]
          .addEventListener("click", async (e) => {
            const url = `http://localhost:8080/test/user/shoppingcart/remove?userid=${userId}&productid=${p.productID}&size=${p.size}&quantity=${p.quantity}`;
            await fetch(url, {
              method: "post",
              headers: {
                "content-type": "application/json",
              },
            });

            newItem.remove();
          });

        shoppingcartParent.appendChild(newItem);
      }
      totalAmt.innerText = "$" + tAmt;
    }
  } catch (err) {
    console.log(err);
  }
};
shopCanvas.addEventListener("show.bs.offcanvas", getAllShoppingCart);
