package com.greenshare.service.color;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.greenshare.entity.vegetable.Color;
import com.greenshare.helpers.IsHelper;
import com.greenshare.repository.ColorRepository;

/**
 * Service class implementation of
 * {@link com.greenshare.service.color.ColorService}
 * 
 * @author joao.silva
 */
@Service
public class ColorServiceImpl extends IsHelper implements ColorService {

	@Autowired
	ColorRepository colorRepository;

	@Override
	public ResponseEntity<?> save(Color color) {
		if (isNotNull(color)) {
			Color colorDB = colorRepository.findOneByName(color.getName());
			if (isNull(colorDB)) {
				Color newColor = new Color(color.getName());
				return newColor.isValid() ? new ResponseEntity<Color>(colorRepository.save(newColor), HttpStatus.OK)
						: new ResponseEntity<List<String>>(newColor.getValidationErrors(), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>("Nome de cor já cadastrado.", HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>("Cor inválida.", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		if (isNotNull(id)) {
			colorRepository.delete(id);
			return new ResponseEntity<String>("Cor deletada.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("ID não pode ser nulo.", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> findOne(Long id) {
		if (isNotNull(id)) {
			Color colorDB = colorRepository.findOne(id);
			if (isNotNull(colorDB)) {
				return new ResponseEntity<Color>(colorDB, HttpStatus.OK);
			}
			return new ResponseEntity<String>("Cor não encontrada.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("ID não pode ser nulo.", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> findAll() {
		Iterable<Color> colorsDB = colorRepository.findAll();
		return new ResponseEntity<Iterable<Color>>(colorsDB, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> update(Color color) {
		if (isNotNull(color)) {
			Color colorDB = colorRepository.findOne(color.getId());
			if (isNotNull(colorDB)) {
				colorDB.update(color);
				if (colorDB.isValid()) {
					colorDB = colorRepository.save(colorDB);
					return new ResponseEntity<Color>(colorDB, HttpStatus.OK);
				}
				return new ResponseEntity<List<String>>(colorDB.getValidationErrors(), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>("Cor não encontrada.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Cor não pode ser nula.", HttpStatus.BAD_REQUEST);
	}

}
