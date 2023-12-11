package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import projekt.nieruchomosci.entity.ApartmentPhoto;

public interface ApartmentPhotoRepository extends JpaRepository<ApartmentPhoto, Long> {

}
