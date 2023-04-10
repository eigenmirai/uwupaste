package xyz.mirai666.uwupaste.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HttpStatusCode {
    private String code;
    private String message;
    private String description;
    @JsonProperty("spec_title") private String specTitle;
    @JsonProperty("spec_href") private String specHref;

}
