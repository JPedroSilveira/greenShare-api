package com.greenshare.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.greenshare.entity.FlowerShop;
import com.greenshare.entity.user.User;

/**
 * Repository Interface of {@link com.greenshare.entity.FlowerShop}
 * 
 * @author joao.silva
 */
@Repository
public interface FlowerShopRepository extends PagingAndSortingRepository<FlowerShop, Long> {

	FlowerShop findOneByUser(User user);

	FlowerShop findOneByCnpj(String cnpj);

	Iterable<FlowerShop> findAllByAddressCityStateAndEnabledTrue(Long id);

	Iterable<FlowerShop> findAllByAddressCityAndEnabledTrue(Long id);

}
