package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Base64;

@Builder
@With
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "manual")
public class Manual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String machineName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String attachFileUrl;
    private String version;
    private String stat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp regdate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp moddate;

    @Transient
    private String content;


    public String getContent() {

        URI url = URI.create(this.attachFileUrl);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> res = restTemplate.getForEntity(url, byte[].class);
        byte[] bytes = res.getBody();

        return Base64.getEncoder().encodeToString(bytes);
    }
}
