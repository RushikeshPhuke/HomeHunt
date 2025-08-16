package com.rentalApp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rentalApp.dto.PropertyDto;
import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;
import com.rentalApp.entity.Property;
import com.rentalApp.entity.PropertyImage;
import com.rentalApp.repository.BrokerRepository;
import com.rentalApp.repository.OwnerRepository;
import com.rentalApp.repository.PropertyImageRepository;
import com.rentalApp.repository.PropertyRepository;
import com.rentalApp.services.PropertyService;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "http://localhost:3000")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private PropertyRepository propertyRepo;
    
    @Autowired
    private PropertyImageRepository imageRepo;
    
    @Autowired
    private OwnerRepository ownerRepository;
    
    @Autowired
    private BrokerRepository brokerRepository;

 
    @PostMapping("/add")
    public ResponseEntity<?> addProperty(@RequestBody Property property, Authentication authentication) {
        String email = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority()).toList();

        if (roles.contains("ROLE_OWNER")) {
            Owner owner = ownerRepository.findByEmail(email).orElse(null);
            if (owner == null) return ResponseEntity.status(403).body("Owner not found");
            property.setOwner(owner);

            // If broker was chosen from frontend
            if (property.getBroker() != null && property.getBroker().getBrokerId() != null) {
                Broker broker = brokerRepository.findById(property.getBroker().getBrokerId()).orElse(null);
                if (broker != null) {
                    property.setBroker(broker);
                }
            } 
            // else, if owner already has a default broker
            else if (owner.getBrokerId() != null) {
                Broker broker = brokerRepository.findById(owner.getBrokerId()).orElse(null);
                property.setBroker(broker);
            }

        } else if (roles.contains("ROLE_BROKER")) {
            Broker broker = brokerRepository.findByEmail(email).orElse(null);
            if (broker == null) return ResponseEntity.status(403).body("Broker not found");
            property.setBroker(broker);
            // Owner can be null or assigned later
        } else {
            return ResponseEntity.status(403).body("Unauthorized role to add property");
        }

        property.setPostedDate(LocalDateTime.now());
        Property saved = propertyService.addProperty(property);

        return ResponseEntity.status(201).body(saved);
    }


    
//    @GetMapping("/all")
//    public List<PropertyDto> getAllProperties() {
//    	System.out.println("checking value");
//        List<Property> properties = propertyRepo.findAll();
//
//        return properties.stream().map(property -> {
//            PropertyDto dto = new PropertyDto();
//            dto.setPropertyId(property.getPropertyId());
//            dto.setTitle(property.getTitle());
//            dto.setAddress(property.getAddress());
//            dto.setCity(property.getCity());
//            dto.setPrice(property.getPrice());
//            dto.setBedrooms(property.getBedrooms());
//            dto.setBathrooms(property.getBathrooms());
//            dto.setArea(property.getArea());
//            dto.setFurnished(property.isFurnished());
//            dto.setSaleOrRent(property.getSaleOrRent());
//            dto.setPostedDate(property.getPostedDate());
//
//            // Set owner details
//            if (property.getOwner() != null) {
//            	System.out.println("checking owner value");
//                dto.setOwnerName(property.getOwner().getName());
//                dto.setOwnerEmail(property.getOwner().getEmail());
//                dto.setOwnerPhone(property.getOwner().getPhone());
//            }
//
//            List<Property> images = propertyRepo.findAllWithImages();
//
//            // Set image URLs
//            if (property.getImages() != null && !property.getImages().isEmpty()) {
//                PropertyImage firstImage = property.getImages().get(0);
//                if (firstImage.getImageData() != null) {
//                    String base64Image = Base64.getEncoder().encodeToString(firstImage.getImageData());
//                    dto.setImageData(base64Image);
//                }
//            }
//
//            return dto;
//        }).collect(Collectors.toList());
//    }
    
    
    @GetMapping("/all")
    public List<PropertyDto> getAllProperties() {
        List<Property> properties = propertyRepo.findAllWithImagesAndOwner();

        return properties.stream().map(property -> {
            PropertyDto dto = new PropertyDto();
            dto.setPropertyId(property.getPropertyId());
            dto.setTitle(property.getTitle());
            dto.setAddress(property.getAddress());
            dto.setCity(property.getCity());
            dto.setPrice(property.getPrice());
            dto.setBedrooms(property.getBedrooms());
            dto.setBathrooms(property.getBathrooms());
            dto.setArea(property.getArea());
            dto.setFurnished(property.isFurnished());
            dto.setSaleOrRent(property.getSaleOrRent());
            dto.setPostedDate(property.getPostedDate());

            // Owner info
            if (property.getOwner() != null) {
                dto.setOwnerName(property.getOwner().getName());
                dto.setOwnerEmail(property.getOwner().getEmail());
                dto.setOwnerPhone(property.getOwner().getPhone());
            }

            // First image
            if (property.getImages() != null && !property.getImages().isEmpty()) {
                PropertyImage firstImage = property.getImages().get(0);
                if (firstImage.getImageData() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(firstImage.getImageData());
                    dto.setImageData(base64Image);
                }
            } else {
                System.out.println("No images found for property ID: " + property.getPropertyId());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    
    
//    @GetMapping("/all") public ResponseEntity<List<Property>> getAllProperties()
//	 { return ResponseEntity.ok(propertyService.getAllProperties()); }

    
    @PostMapping("/{propertyId}/upload-images")
    public ResponseEntity<String> uploadMultipleImages(
            @PathVariable int propertyId,
            @RequestParam("images") List<MultipartFile> imageFiles) {

        Property property = propertyRepo.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }

        for (MultipartFile file : imageFiles) {
            try {
                PropertyImage image = new PropertyImage();
                image.setImageData(file.getBytes());
                image.setImageType(file.getContentType());
                image.setProperty(property);
                property.getImages().add(image);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process image: " + file.getOriginalFilename());
            }
        }

        propertyRepo.save(property);
        return ResponseEntity.ok("Images uploaded successfully.");
    }

    
	/*
	 * @PostMapping("/{propertyId}/upload-image") public ResponseEntity<?>
	 * uploadImage(
	 * 
	 * @PathVariable int propertyId,
	 * 
	 * @RequestParam("image") MultipartFile file) {
	 * 
	 * Property property = propertyRepo.findById(propertyId).orElse(null); if
	 * (property == null) { return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found"); }
	 * 
	 * try { PropertyImage image = new PropertyImage();
	 * image.setImageData(file.getBytes());
	 * image.setImageType(file.getContentType()); image.setProperty(property);
	 * 
	 * property.getImages().add(image); propertyRepo.save(property); // saves both
	 * due to cascade
	 * 
	 * return ResponseEntity.ok("Image uploaded successfully"); } catch (Exception
	 * e) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Failed to upload image"); } }
	 */
    
   

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int imageId) {
        PropertyImage image = imageRepo.findById(imageId).orElse(null);
        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getImageType()))
                .body(image.getImageData());
    }
    
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<String>> getImageUrlsByProperty(@PathVariable int propertyId) {
        List<PropertyImage> images = imageRepo.findByProperty_PropertyId(propertyId);

        List<String> urls = images.stream()
            .map(img -> "http://localhost:8080/api/images/" + img.getId())
            .toList();

        return ResponseEntity.ok(urls);
    }




   
    public ResponseEntity<?> getProperty(@PathVariable int id) {
        Optional<Property> optionalProperty = propertyService.getPropertyById(id);

        if (optionalProperty.isPresent()) {
            Property property = optionalProperty.get();
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found.");
        }
    }


   
//    @GetMapping("/all")
//    public ResponseEntity<List<Property>> getAllProperties() {
//        return ResponseEntity.ok(propertyService.getAllProperties());
//    }

    // Search/filter properties (optional parameters)
    //@GetMapping("/search")
	/*
	 * public ResponseEntity<List<Property>> searchProperties(
	 * 
	 * @RequestParam(required = false, defaultValue = "") String city,
	 * 
	 * @RequestParam(required = false, defaultValue = "") String type,
	 * 
	 * @RequestParam(required = false, defaultValue = "9999999") float maxPrice ) {
	 * List<Property> results = propertyService.searchProperties(city, type,
	 * maxPrice); return ResponseEntity.ok(results); }
	 */
    
    @GetMapping("/rent")
    public ResponseEntity<List<Property>> getRentProperties() {
        List<Property> rentProperties = propertyRepo.findPropertiesBySaleOrRent("rent");
        return ResponseEntity.ok(rentProperties);
    }

    @GetMapping("/sale")
    public ResponseEntity<List<Property>> getSaleProperties() {
        List<Property> saleProperties = propertyRepo.findPropertiesBySaleOrRent("sale");
        return ResponseEntity.ok(saleProperties);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<Property>> filterProperties(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) Integer bedrooms) {

        List<Property> filtered = propertyRepo.filterProperties(
            type, city, min, max, bedrooms
        );

        return ResponseEntity.ok(filtered);
    }
    
  //get broker by property
    @GetMapping("/{propertyId}/broker")
    public ResponseEntity<Broker> getBrokerByPropertyId(@PathVariable int propertyId) {
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));

        Broker broker = property.getBroker();
        if (broker == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(broker);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Property>> getPropertiesByOwner(@PathVariable int ownerId) {
        List<Property> ownerProperties = propertyRepo.findByOwner_OwnerId(ownerId);
        return ResponseEntity.ok(ownerProperties);
    }

 
    @GetMapping("/broker/{brokerId}")
    public ResponseEntity<List<Property>> getPropertiesByBroker(@PathVariable int brokerId) {
        List<Property> properties = propertyRepo.findByBroker_BrokerId(brokerId);
        return ResponseEntity.ok(properties);
    }
    
    //changed
    @PostMapping("/add/{ownerId}")
    public ResponseEntity<Property> addPropertyForOwner(
            @PathVariable int ownerId,
            @RequestBody Property propertyRequest) {

        Optional<Owner> ownerOpt = ownerRepository.findById(ownerId);
        if (!ownerOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Owner owner = ownerOpt.get();

        // Set owner and broker from owner object
        propertyRequest.setOwner(owner);
        
       // propertyRequest.setBroker(owner.getBrokerId());

        Property saved = propertyRepo.save(propertyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }



}

