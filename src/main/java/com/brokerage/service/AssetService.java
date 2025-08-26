package com.brokerage.service;

import com.brokerage.entity.Asset;
import com.brokerage.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AssetService {
    
    @Autowired
    private AssetRepository assetRepository;
    
    public List<Asset> getAssetsByCustomer (String customerId) {
        return assetRepository.findByCustomerId (customerId);
    }
    
    public Asset getOrCreateAsset (String customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName (customerId, assetName)
            .orElseGet(() -> {
                Asset newAsset = new Asset(customerId, assetName, BigDecimal.ZERO, BigDecimal.ZERO);
                return assetRepository.save(newAsset);
            });
    }
    
    public void addAssetBalance (String customerId, String assetName, BigDecimal amount) {
        Asset asset = getOrCreateAsset(customerId, assetName);
        asset.setSize (asset.getSize().add(amount));
        asset.setUsableSize (asset.getUsableSize().add(amount));
        assetRepository.save(asset);
    }
    
    public void reduceAssetBalance (String customerId, String assetName, BigDecimal amount) {
        Asset asset = getOrCreateAsset(customerId, assetName);
        if (asset.getUsableSize().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for " + assetName);
        }
        asset.setUsableSize (asset.getUsableSize().subtract(amount));
        assetRepository.save(asset);
    }

}
