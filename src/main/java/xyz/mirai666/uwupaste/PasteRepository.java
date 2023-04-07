package xyz.mirai666.uwupaste;

import org.springframework.data.repository.CrudRepository;
import xyz.mirai666.uwupaste.model.Paste;

public interface PasteRepository extends CrudRepository<Paste, String> {
}
