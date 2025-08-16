package com.rentalApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rentalApp.entity.Owner;
import com.rentalApp.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Integer>{

	List<Property> findByCityContainingIgnoreCaseAndPropertyTypeContainingIgnoreCaseAndPriceLessThanEqual(String city,
			String type, float maxPrice);
	
	@Query("SELECT p FROM Property p WHERE LOWER(p.saleOrRent) = LOWER(:saleOrRent)")
	List<Property> findPropertiesBySaleOrRent(@Param("saleOrRent") String saleOrRent);
	
	List<Property> findByOwner_OwnerId(int ownerId);

	// PropertyRepository.java
	List<Property> findByBroker_BrokerId(int brokerId);

	@Query("SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.owner")
	List<Property> findAllWithImagesAndOwner();



	// query  for filter all the properties.
		 @Query("SELECT p FROM Property p WHERE " +
		            "(:saleOrRent IS NULL OR LOWER(p.saleOrRent) = LOWER(:saleOrRent)) AND " +
		            "(:city IS NULL OR LOWER(p.city) = LOWER(:city)) AND " +
		            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
		            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
		            "(:bedrooms IS NULL OR p.bedrooms = :bedrooms)")
		    List<Property> filterProperties(
		            @Param("saleOrRent") String saleOrRent,
		            @Param("city") String city,
		            @Param("minPrice") Double minPrice,
		            @Param("maxPrice") Double maxPrice,
		            @Param("bedrooms") Integer bedrooms
		    );

}