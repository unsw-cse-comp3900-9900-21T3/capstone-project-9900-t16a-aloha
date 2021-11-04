import { removeAllChilds } from "./helper.js";
const orderItemParent = document.getElementById("order-history-list");
const orderItemProductParentTemp =
  document.getElementById("order-history-item");

const orderProductTemp = document.getElementById("order-product-item");

const getAllOrders = () => {
  removeAllChilds(orderItemParent);
  //   const url = `http://localhost:8080/test/user/${sessionStorage.getItem(
  //     "userID"
  //   )}/order`;
  try {
    // const response = await fetch(url, {
    //   method: "get",
    //   headers: {
    //     "content-type": "application/json",
    //   },
    // });
    // const jsData = await response.text();
    // const d = JSON.parse(jsData);
    // console.log("Completed!", d);
    // if (d.status === "fail") {
    //   alert("Something Wrong");
    // } else {
    // }
    // for (let order in d.orders){

    // }

    let d = {
      orderId: "abc",
      phone: "423526851",
      firstName: "Peng",
      lastName: "Yang",
      street: "2 Waterways st",
      city: "WWP",
      postcode: "2127",
      state: "NSW",
      products: [
        {
          id: 1,
          size: 2,
          qty: 3,
        },
        {
          id: 2,
          size: 2,
          qty: 3,
        },
      ],
    };
    const anOrder = orderItemProductParentTemp.cloneNode(true);
    anOrder.classList.add("order-history-item-card");
    anOrder.getElementsByClassName("order-history-item-number")[0].innerText =
      "Order #" + d.orderId;
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
    for (let p of d.products) {
      const orderProduct = orderProductTemp.cloneNode(true);
      orderProduct.id = d.orderId + "_" + p.id;
      orderProduct.getElementsByClassName("shop-qty")[0].innerText = p.qty;
      orderProduct.getElementsByClassName("shop-size")[0].innerText =
        "US " + p.size;
      anOrder
        .getElementsByClassName("order-product-list")[0]
        .appendChild(orderProduct);
    }

    orderItemParent.appendChild(anOrder);
  } catch (err) {
    console.log(err);
  }
};

export { getAllOrders };
