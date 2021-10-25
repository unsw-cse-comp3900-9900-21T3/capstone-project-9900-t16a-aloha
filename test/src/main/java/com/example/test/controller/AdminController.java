package com.example.test.controller;

import com.example.test.model.ImgUrl;
import com.example.test.model.Product;
import com.example.test.model.Storge;
import com.example.test.model.StorgeId;
import com.example.test.repository.ProductRepository;
import com.example.test.repository.ShoppingCartRepository;
import com.example.test.repository.StorgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//import jdk.internal.org.jline.utils.ExecHelper;

import java.util.*;

import javax.print.FlavorException;

@Controller
@RequestMapping(path ="/admin")
public class AdminController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    StorgeRepository storgeRepository;

    @GetMapping(path = "/showall")
    @CrossOrigin
    public @ResponseBody Iterable<Product> getAllProduct(@RequestParam(name="pageindex", defaultValue = "0") Integer pageIndex,
                                                         @RequestParam(name="pagesize", defaultValue = "8") Integer pageSize) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        return productRepository.findAll(paging);
    }

    // delete a product
    // latest update: upon remove, set the 'isDeleted' attribute to 1 instead of 'visibility'
    @PostMapping(path = "/remove")
    @CrossOrigin
    public @ResponseBody Map<String, Object> removeProduct(@RequestParam String productid) {
        Map<String, Object> resBody = new HashMap<>(3);
        Optional<Product> products = productRepository.findById(productid);
        // product not exist or already removed
        if (!products.isPresent()|| products.get().getIsDeleted()==1) {
            resBody.put("status", "fail");
            resBody.put("msg", "product does not exist");

        }
        else {
            Product product = products.get();
            product.setIsDeleted(1);
            product.setVisibility(0);
            productRepository.save(product);
            resBody.put("status", "success");
        }
        return resBody;
    }

    // show all storge
    @GetMapping(path = "/showallstorge")
    @CrossOrigin
    public @ResponseBody Iterable<Storge> getAllStorge() {
        Iterable<Storge> test = storgeRepository.findAll();
        return storgeRepository.findAll();
    }

    /**
     * Aim: add storge to storge table only (assume the product is already inside the product table)
     * @param id
     * @param size
     * @param stock
     * @return storge json
     */
    @PostMapping(path = "/add")
    @CrossOrigin
    public @ResponseBody Storge addProductToStorge(@RequestParam String id, @RequestParam String size, @RequestParam String stock) {
            Optional<Product> product = productRepository.findById(id);
            Product p = product.get();
            StorgeId si = new StorgeId();
            si.setProduct(p);
            si.setSize(Float.parseFloat(size));

            Storge s = new Storge();
            s.setStorgeid(si);
            s.setStock(Integer.parseInt(stock));
            
            return this.storgeRepository.save(s);
    }

    /**
     * Aim: add product to product table and add storge to storge table
     * @param id
     * @param p
     * @return product json
     */
    @PostMapping(path = "/add/{id}")
    @CrossOrigin
    public @ResponseBody Product addProduct(@PathVariable String id, @RequestParam int visibility ,@RequestBody Product p) {
            // save product to the product table
            Product addProduct = new Product();
            addProduct.setVisibility(visibility);
            addProduct.setId(id);
            addProduct.setAvgRating(p.getAvgRating());

            addProduct.setDiscount(p.getDiscount());
            addProduct.setIsDeleted(p.getIsDeleted());
            addProduct.setPrice(p.getPrice());
            addProduct.setDescription(p.getDescription());
            addProduct.setName(p.getName());
            addProduct.setBrand(p.getBrand());
            addProduct.setImgURL(p.getImgURL());
            Product saveProduct = this.productRepository.save(addProduct);

//            StorgeId si = new StorgeId();
//            si.setProduct(addProduct);
//            si.setSize(Float.parseFloat(size));
//
//            Storge s = new Storge();
//            s.setStorgeid(si);
//            s.setStock(Integer.parseInt(stock));
//            this.storgeRepository.save(s);

            return saveProduct;
    }

    /* Search a product
        Possible requests:
            (1) Search by productID: /search/id/{id}
            (2) Search by name: /search/name/{name}

        ANNOTATION:
        depending on the data pass from frontend, we might change the implementation
        also, thinking combining all the searching inside the single function (depending on front end design)
     */
    @GetMapping(path = "/search/id/{id}")
    @CrossOrigin
    public @ResponseBody Product searchByID(@PathVariable("id") String id) {
        return productRepository.findById(id).get();
    }

    
    @GetMapping(path = "/search/name/{name}")
    @CrossOrigin
    public @ResponseBody Iterable<Product> searchByName(@PathVariable("name") String name) {
        return productRepository.findProductsByNameContaining(name);
    }

    // ANNOTATION:
    // not sure if the return type is correct since if we cannot parse float properly
    // searching the price between price1 and price2
    @GetMapping(path = "/search/price/{price1}-{price2}")
    @CrossOrigin
    public @ResponseBody Iterable<Product> searchByPrice(@PathVariable("price1") String price1,
                                                         @PathVariable("price2") String price2) {
        try {
            float p1 = Float.parseFloat(price1);
            float p2 = Float.parseFloat(price2);
            return productRepository.findByPriceBetween(p1, p2);
        } catch (Exception e) {
            return null;
        }
    }

    // update a product's information(not include image URL)

    @PostMapping(path = "/update")
    @CrossOrigin
    public @ResponseBody Map<String, Object> update(@RequestParam(value = "id") String id,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "price", required = false) String price,
                                                    @RequestParam(value = "discount",required = false) String discount,
                                                    @RequestParam(value = "brand", required = false) String brand,
                                                    @RequestParam(value = "desc", required = false) String desc,
                                                    @RequestParam(value = "visibility", required = false) Integer visibility,
                                                    @RequestBody ImgUrl imgurl) {
        Map<String, Object> resBody = new HashMap<>(3);
        Optional<Product> products = productRepository.findById(id);
        if(!products.isPresent()) {
            resBody.put("status", "fail");
            resBody.put("msg", "Product does not exist");
            return resBody;
        }
        Product product = products.get();
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
        if(imgurl != null) product.setImgURL(imgurl.getImgurls());
        if(visibility !=null) product.setVisibility(visibility);
        productRepository.save(product);
        resBody.put("status", "success");
        return resBody;
    }

    /**
     * Add an image to current product
     * @param url
     * @param productid
     * @return
     */
    @PostMapping(path = "/updateimg/add")
    @CrossOrigin
    public @ResponseBody Map<String, Object> addImage(@RequestParam(name = "url") String url, @RequestParam(name = "productid") String productid) {
        Map<String, Object> resBody = new LinkedHashMap<>(3);
        Optional<Product> product = productRepository.findById(productid);

        String imgurl = product.get().getImgURL();
        // the product does not have images
        if (imgurl == null) {
            String res ="";
            res += "[";
            res += "\""+url+"\"";
            res += "]";
            product.get().setImgURL(res);
            productRepository.save(product.get());
            resBody.put("msg","succeed");
        }
        else {
            // remove "[" and "]"
            String img = imgurl.substring(1,imgurl.length()-1);
            String[] temp = img.split(",");
            String res = "[";
            for(String s: temp) {
                res += s;
                if(s.equals("\""+url+"\"")) {
                    resBody.put("status","failed");
                    resBody.put("msg", "This image has already existed");
                    return resBody;
                }
            }
            res += ","+"\""+url+"\"]";
            product.get().setImgURL(res);
            productRepository.save(product.get());
            resBody.put("msg","succeed");
        }
        return resBody;


    };

    /**
     * Delete an image from current product
     * @param url
     * @param productid
     * @return
     */
    @PostMapping(path = "/updateimg/del")
    @CrossOrigin
    public @ResponseBody Map<String, Object> delImage(@RequestParam String url, @RequestParam String productid) {
        Map<String,Object> resBody = new LinkedHashMap<>(3);
        Optional<Product> product = productRepository.findById(productid);
        String imgurl = product.get().getImgURL();
        String img = imgurl.substring(1,imgurl.length()-1);
        String[] temp = img.split(",");
        String res = "[";
        boolean found = false;
        for(String s: temp) {

            if(!s.equals("\""+url+"\"")) {
                res += s+",";
            }
            else {
                found = true;
            }
        }
        res = res.substring(0,res.length()-1);
        res += "]";
        if(!found) {
            resBody.put("status", "failed");
            resBody.put("msg","Image not found");
        }
        else {
            product.get().setImgURL(res);
            productRepository.save(product.get());
            resBody.put("msg", "succeed");
        }
        return resBody;


    }


}
