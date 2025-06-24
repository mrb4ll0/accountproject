package com.general;


import com.general.AssetDisposal;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad
 */
 @FacesConverter(value = "assetSelectConverter")
    public  class AssetSelectConverter implements Converter {

        @Override
        public AssetDisposal.Asset getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }

            
            AssetDisposal bean = context.getApplication()
                    .evaluateExpressionGet(context, "#{assetDisposalBean}", AssetDisposal.class);
            
            if (bean == null) {
                return null;
            }

            List<AssetDisposal.Asset> assets = bean.getAvailableAssets();
            if (assets == null) return null;
            
            return assets.stream()
                    .filter(a -> a.getAssetCode().equals(value))
                    .findFirst()
                    .orElse(null);
        }

       

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
         if (value == null) {
        return "";
    }

    if (value instanceof AssetDisposal.Asset) {
        AssetDisposal.Asset asset = (AssetDisposal.Asset) value;
        return asset.getAssetCode(); 
    }

    return "";
    }
    }