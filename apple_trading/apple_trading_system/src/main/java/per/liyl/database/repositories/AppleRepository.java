package per.liyl.database.repositories;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import per.liyl.database.entities.AppleOrder;

import java.util.Optional;
import java.util.function.Function;

@Repository
public interface AppleRepository extends JpaRepository<AppleOrder, Long> {

    @Override
    <S extends AppleOrder> Optional<S> findOne(Example<S> example);

    @Override
    <S extends AppleOrder> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    <S extends AppleOrder> long count(Example<S> example);

    @Override
    <S extends AppleOrder> boolean exists(Example<S> example);

    @Override
    <S extends AppleOrder, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);

    @Override
    <S extends AppleOrder> S save(S entity);

    @Override
    Optional<AppleOrder> findById(Long aLong);

    @Override
    void deleteById(Long aLong);
}
