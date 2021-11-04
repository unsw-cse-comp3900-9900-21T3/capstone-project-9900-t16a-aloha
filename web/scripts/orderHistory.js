import { removeAllChilds } from "./helper.js";
import { getProductDetail } from "./gotoUserProductDetailPage.js";
const orderItemParent = document.getElementById("order-history-list");
const orderItemProductParentTemp =
  document.getElementById("order-history-item");

const orderProductTemp = document.getElementById("order-product-item");

const getAllOrders = async () => {
  removeAllChilds(orderItemParent);
  const url = `http://localhost:8080/test/getOrders/${sessionStorage.getItem(
    "userID"
  )}`;
  try {
    const response = await fetch(url, {
      method: "get",
      headers: {
        "content-type": "application/json",
      },
    });
    const jsData = await response.text();
    const orders = JSON.parse(jsData);
    console.log("Completed! get orders", orders);
    for (let orderIdx in orders) {
      const d = orders[orderIdx];
      console.log("order:", d);
      const anOrder = orderItemProductParentTemp.cloneNode(true);
      anOrder.removeAttribute("id");
      anOrder.classList.add("order-history-item-card");
      anOrder.getElementsByClassName("order-history-item-number")[0].innerText =
        "Order #" + d.orderid;
      anOrder.getElementsByClassName("order-history-item-name")[0].innerText =
        d.firstName + " " + d.lastName;
      anOrder.getElementsByClassName("order-history-item-addr")[0].innerText =
        d.street;
      anOrder.getElementsByClassName("order-history-item-phone")[0].innerText =
        d.phone;
      anOrder.getElementsByClassName("order-history-item-city")[0].innerText =
        d.city;
      anOrder.getElementsByClassName("order-history-item-state")[0].innerText =
        d.state;
      anOrder.getElementsByClassName("order-history-item-zip")[0].innerText =
        d.postcode;
      anOrder.getElementsByClassName("accordion-collapse")[0].id =
        "order_" + d.orderid;
      anOrder
        .getElementsByClassName("accordion-button")[0]
        .setAttribute("data-bs-target", "#" + "order_" + d.orderid);
      let totalPrice = 0;

      for (let p of d.products) {
        const url = `http://localhost:8080/admin/search/id/${p.id}`;
        const response = await fetch(url, {
          method: "get",
          headers: {
            "content-type": "application/json",
          },
        });

        const jsData = await response.text();
        const pDetail = JSON.parse(jsData);

        const orderProduct = orderProductTemp.cloneNode(true);
        orderProduct.id = d.orderid + "_" + p.id;
        orderProduct.getElementsByClassName("shop-name")[0].innerText =
          pDetail.name;
        orderProduct.getElementsByClassName("shop-price")[0].innerText =
          "$ " + pDetail.price;
        orderProduct.getElementsByClassName("total-price")[0].innerText =
          "$ " + parseFloat(pDetail.price) * parseInt(p.qty, 10);
        totalPrice += parseFloat(pDetail.price) * parseInt(p.qty, 10);
        orderProduct.getElementsByClassName("shop-qty")[0].innerText = p.qty;
        orderProduct.getElementsByClassName("shop-size")[0].innerText =
          "US " + p.size;
        if (pDetail.imgURL != null && pDetail.imgURL != "[]") {
          const imgsArrayJson = JSON.parse(pDetail.imgURL);
          orderProduct.getElementsByClassName("order-product-item-img")[0].src =
            imgsArrayJson[0];
        }

        anOrder
          .getElementsByClassName("order-product-list")[0]
          .appendChild(orderProduct);
      }
      anOrder.getElementsByClassName("order-total-price")[0].innerText =
        "$" + totalPrice;
      orderItemParent.appendChild(anOrder);
    }
  } catch (err) {
    console.log(err);
  }
};

export { getAllOrders };
