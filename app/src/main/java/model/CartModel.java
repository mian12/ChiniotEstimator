package model;

/**
 * Created by Mian Shahbaz Idrees on 12-Mar-18.
 */

public class CartModel {

    private  String vrnoa;
    private  String itemName;
    private  String menuRate;
    private  String uom;
    private  String itemPackage;
    private  String itemId;
    private  String menuId;


    public String getVrnoa() {
        return vrnoa;
    }

    public void setVrnoa(String vrnoa) {
        this.vrnoa = vrnoa;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMenuRate() {
        return menuRate;
    }

    public void setMenuRate(String menuRate) {
        this.menuRate = menuRate;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getItemPackage() {
        return itemPackage;
    }

    public void setItemPackage(String itemPackage) {
        this.itemPackage = itemPackage;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
