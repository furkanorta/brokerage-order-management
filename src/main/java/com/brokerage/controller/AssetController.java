package com.brokerage.controller;

import com.brokerage.entity.Asset;
import com.brokerage.service.AssetService;
//import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@Tag(name = "Assets", description = "Asset management endpoints")
public class AssetController {
    
    @Autowired
    private AssetService assetService;
    
    @GetMapping
    @Operation(summary = "List assets", description = "Get all assets for a customer")
    public ResponseEntity<List<Asset>> getAssets (@RequestParam(required = false) String customerId) {
        if (customerId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            customerId = auth.getName();
        }
        
        List<Asset> assets = assetService.getAssetsByCustomer (customerId);
        return ResponseEntity.ok(assets);
    }

}
