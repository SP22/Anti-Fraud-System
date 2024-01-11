package antifraud.service;

import antifraud.entity.Ip;
import antifraud.exceptions.DuplicateEntityException;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.repository.IpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpService {

    @Autowired
    private IpRepository ipRepository;

    public Ip create(Ip ip) {
        if (null == ipRepository.findByIp(ip.getIp())) {
            return ipRepository.save(ip);
        }
        throw new DuplicateEntityException("IP already exists");
    }

    public List<Ip> getIpList() {
        return ipRepository.findAll();
    }

    public void delete(String ip) {
        Ip entity = ipRepository.findByIp(ip);
        if (null == entity) {
            throw new EntityNotFoundException();
        }
        ipRepository.delete(entity);
    }

    public boolean existsByIp(String ip) {
        return ipRepository.existsByIp(ip);
    }
}
