package fr.thomas.orm;

import java.util.List;
import java.util.Optional;

import fr.thomas.orm.interfaces.DAO;

public class Model<T> implements DAO<T>{

	public T create(T object) {
		// TODO Auto-generated method stub
		return null;
	}

	public T update(T object) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(T object) {
		// TODO Auto-generated method stub
		
	}

	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<T> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
