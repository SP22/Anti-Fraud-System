package antifraud.repository;

import antifraud.entity.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepository extends JpaRepository<Ip, Long> {
    Ip findByIp(String ip);

    boolean existsByIp(String ip);
}
