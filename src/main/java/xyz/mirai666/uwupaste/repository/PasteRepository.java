package xyz.mirai666.uwupaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.mirai666.uwupaste.model.entity.Paste;

public interface PasteRepository extends JpaRepository<Paste, String> {
}
