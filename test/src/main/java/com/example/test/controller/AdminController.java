package com.example.test.controller;

import com.example.test.model.Product;
import com.example.test.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//import jdk.internal.org.jline.utils.ExecHelper;

import java.util.HashMap;
import java.util.Map;

import javax.print.FlavorException;

@Controller
@RequestMapping(path ="/admin")
public class AdminController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping(path = "/showall")
    public @ResponseBody Iterable<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @PostMapping(path = "/remove")
    public @ResponseBody Map<String, Object> removeProduct(@RequestParam String productID) {
        Map<String, Object> resBody = new HashMap<>(3);
        Product product = productRepository.findById(productID);
        // product not exist or already removed
        if (product == null || product.getVisibility()==0) {
            resBody.put("status", "fail");

        }
        else {
            product.setVisibility(0);
            productRepository.save(product);
            resBody.put("status", "success");
        }
        return resBody;
    }

    /* Search a product
        Possible requests:
            (1) Search by productID: /search/id/{id}
            (2) Search by name: /search/name/{name}

        Annotation: 
        depending on the data pass from frontend, we might change the implementation
        also, thinking combining all the searching inside the single function (depending on front end design)
     */
    @GetMapping(path = "/search/id/{id}")
    public @ResponseBody Product searchByID(@PathVariable("id") String id) {
        return productRepository.findById(id);
    }

    
    @GetMapping(path = "/search/name/{name}")
    public @ResponseBody Iterable<Product> searchByName(@PathVariable("name") String name) {
        return productRepository.findProductsByNameContaining(name);
    }

    // Annotation:
    // not sure if the return type is correct since if we cannot parse float properly
    // searching the price between price1 and price2
    @GetMapping(path = "/search/price/{price1}-{price2}")
    public @ResponseBody Iterable<Product> searchByPrice(@PathVariable("price1") String price1,
                                                         @PathVariable("price2") String price2) {
        try {
            float p1 = Float.parseFloat(price1);
            float p2 = Float.parseFloat(price2);
            return productRepository.findByPriceBetween(Float.parseFloat(price1), Float.parseFloat(price2));
        } catch (Exception e) {
            return null;
        }
    }

    // update a product's information(not include image URL)
    @PostMapping(path = "/update")
    public @ResponseBody Map<String, Object> update(@RequestParam(value = "id") String id,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "price", required = false) String price,
                                                    @RequestParam(value = "discount",required = false) String discount,
                                                    @RequestParam(value = "brand", required = false) String brand,
                                                    @RequestParam(value = "desc", required = false) String desc) {
        Map<String, Object> resBody = new HashMap<>(3);
        Product product = productRepository.findById(id);
        if(product == null) {
            resBody.put("status", "fail");
            resBody.put("msg", "Product does not exist");
        }
        // check price format
        if(price != null) {
            try {
                float p = Float.parseFloat(price);
                product.setPrice(p);
            }
            catch (Exception e) {
                resBody.put("status", "fail");
                resBody.put("msg", e.getMessage());
                return resBody;
            }
        }
        // check discount format
        if(discount != null) {
            try {
                float d = Float.parseFloat(discount);
                product.setDiscount(d);
            }
            catch (Exception e) {
                resBody.put("status", "fail");
                resBody.put("msg", e.getMessage());
                return resBody;
            }
        }
        // update attributes(if exists)
        if(name != null) product.setName(name);
        if(brand != null) product.setBrand(brand);
        if(desc != null) product.setDescription(desc);
        productRepository.save(product);
        resBody.put("status", "success");
        return resBody;
    }

    // TODO: add a image on current Product
    @PostMapping(path = "update/img/add/{url}")
    public @ResponseBody Map<String, Object> addImage(@PathVariable("url") String url) {
        return  new HashMap<>(1);
    };
    // TODO: delete a image on current Product
    @PostMapping(path = "update/img/del/{url}")
    public @ResponseBody Map<String, Object> delImage(@PathVariable("url") String url) {
        return new HashMap<>(1);
    }


}
