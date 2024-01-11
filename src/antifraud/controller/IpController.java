package antifraud.controller;

import antifraud.entity.Ip;
import antifraud.service.IpService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
@Validated
public class IpController {
    @Autowired
    private IpService ipService;

    @PostMapping(value = {"/", ""})
    public ResponseEntity<Ip> addSuspiciousIp(@RequestBody @Valid IpRequest ipRequest) {
        Ip ip = new Ip();
        ip.setIp(ipRequest.ip());
        ip = ipService.create(ip);
        return ResponseEntity.ok(ip);
    }

    @GetMapping(value = {"", "/"})
    public List<Ip> getIps() {
        return ipService.getIpList();
    }

    @DeleteMapping("/{ip}")
    public ResponseEntity<Map<String, String>> deleteIp(@Pattern(regexp = "((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}") @PathVariable("ip") String ip) {
        ipService.delete(ip);
        return ResponseEntity.ok(Map.of("status", String.format("IP %s successfully removed!", ip)));
    }
}

record IpRequest(@NotBlank @Pattern(regexp = "((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}") String ip) {}
