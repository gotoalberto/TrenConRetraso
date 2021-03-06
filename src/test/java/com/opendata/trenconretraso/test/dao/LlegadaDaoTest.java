package com.opendata.trenconretraso.test.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.jdo.annotations.NotPersistent;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.opendata.trenconretraso.bom.Estacion;
import com.opendata.trenconretraso.bom.Indemnizacion;
import com.opendata.trenconretraso.bom.Llegada;
import com.opendata.trenconretraso.bom.TipoTren;
import com.opendata.trenconretraso.dao.EstacionDao;
import com.opendata.trenconretraso.dao.LlegadaDao;
import com.opendata.trenconretraso.dao.TipoTrenDao;
import com.opendata.trenconretraso.test.BaseSpringTest;

/**
 * 
 * @author Alberto Gomez Toribio
 *
 */
public class LlegadaDaoTest extends BaseSpringTest{
	
	@Autowired
	LlegadaDao llegadaDao;
	@Autowired
	EstacionDao estacionDao;
	@Autowired
	TipoTrenDao tipoTrenDao;
	
	@Test
	@NotPersistent
	public void findByEstacion(){
	
		Estacion estacion = new Estacion();
		estacion.setCodigo(60400L)  ;
		estacion.setNombre("ALCAZAR DE SAN JUAN");
		estacion.setURL("http://www.adif.es/AdifWeb/estacion_mostrar.jsp?e=60400&t=E");
		
		estacion = estacionDao.create(estacion);
		
		Llegada llegada = new Llegada();
		llegada.setIdEstacion(estacion.getId());
		llegada.sethLlegada(new Date());
		llegada.sethPrevista(new Date());
		llegada.setNumeroTren(17000L);
		
		TipoTren tipoTren = new TipoTren();
		tipoTren.setNombreADIF("MD");
		List<Indemnizacion> indemnizaciones = new ArrayList<Indemnizacion>();
		tipoTren.setIndemnizaciones(indemnizaciones);
		
		llegada.setTipoTren(tipoTren);
		llegada = llegadaDao.create(llegada);
		
		Calendar desdec = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		desdec.set(Calendar.HOUR_OF_DAY,0);
		desdec.set(Calendar.MINUTE,0);
		desdec.set(Calendar.SECOND,0);
		desdec.set(Calendar.MILLISECOND,0);
		
		Calendar hastac = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		hastac.set(Calendar.HOUR_OF_DAY,23);
		hastac.set(Calendar.MINUTE,59);
		hastac.set(Calendar.SECOND,59);
		hastac.set(Calendar.MILLISECOND,0);
		
		List<Llegada> llegadas = llegadaDao.findByEstacion(estacion.getId(),desdec.getTime(),hastac.getTime());
		Assert.assertTrue(llegadas.size() == 1);
		Llegada a = llegadas.get(0);
		log.debug(a.getTipoTren().getNombreADIF());
		
		desdec.add(Calendar.DAY_OF_MONTH, 2);
		hastac.add(Calendar.DAY_OF_MONTH, 2);
		
		
		llegadas = llegadaDao.findByEstacion(estacion.getId(),desdec.getTime(),hastac.getTime());
		Assert.assertTrue(llegadas == null || llegadas.size() == 0);
	}
	
	@Test
	@NotPersistent
	public void findLastByTren(){
		
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		
		Estacion estacion = new Estacion();
		estacion.setCodigo(60400L);
		estacion.setNombre("ALCAZAR DE SAN JUAN");
		estacion.setURL("http://www.adif.es/AdifWeb/estacion_mostrar.jsp?e=60400&t=E");
		
		estacion = estacionDao.create(estacion);
		
		Llegada llegada = new Llegada();
		llegada.setIdEstacion(estacion.getId());
		llegada.sethLlegada(date.getTime());
		llegada.sethPrevista(date.getTime());
		llegada.setNumeroTren(17000L);
		llegada = llegadaDao.create(llegada);
		
		date.add(Calendar.SECOND, 1);
		
		Llegada llegada2 = new Llegada();
		llegada2.setIdEstacion(estacion.getId());
		llegada2.sethLlegada(date.getTime());
		llegada2.sethPrevista(date.getTime());
		llegada2.setNumeroTren(17000L);
		llegada2 = llegadaDao.create(llegada2);
		
		Llegada llegadaFound = llegadaDao.findUltimaLlegadaDeTrenAEstacion(estacion.getId(), llegada.getNumeroTren());
		
		Assert.assertTrue(llegadaFound.getId() == llegada2.getId());
	}
	
	@Test
	public void findByIdTest(){
		
		Estacion estacion = new Estacion();
		estacion.setCodigo(60400L)  ;
		estacion.setNombre("ALCAZAR DE SAN JUAN");
		estacion.setURL("http://www.adif.es/AdifWeb/estacion_mostrar.jsp?e=60400&t=E");
		
		estacion = estacionDao.create(estacion);
		
		Llegada llegada = new Llegada();
		llegada.setIdEstacion(estacion.getId());
		llegada.sethLlegada(new Date());
		llegada.sethPrevista(new Date());
		llegada.setNumeroTren(17000L);
		
		TipoTren tipoTren = new TipoTren();
		tipoTren.setNombreADIF("MD");
		List<Indemnizacion> indemnizaciones = new ArrayList<Indemnizacion>();
		tipoTren.setIndemnizaciones(indemnizaciones);
		
		llegada.setTipoTren(tipoTren);
		llegada = llegadaDao.create(llegada);
		
		assertTrue(llegadaDao.findById(llegada.getId().getId()) != null);
	}
	
	@Test
	public void findByTipoTrenTest(){
		
		Estacion estacion = new Estacion();
		estacion.setCodigo(60400L)  ;
		estacion.setNombre("ALCAZAR DE SAN JUAN");
		estacion.setURL("http://www.adif.es/AdifWeb/estacion_mostrar.jsp?e=60400&t=E");
		
		estacion = estacionDao.create(estacion);
		
		Llegada llegada = new Llegada();
		llegada.setIdEstacion(estacion.getId());
		llegada.sethLlegada(new Date());
		llegada.sethPrevista(new Date());
		llegada.setNumeroTren(17000L);
		
		TipoTren tipoTren = new TipoTren();
		tipoTren.setNombreADIF("MD");
		List<Indemnizacion> indemnizaciones = new ArrayList<Indemnizacion>();
		tipoTren.setIndemnizaciones(indemnizaciones);
		
		llegada.setTipoTren(tipoTren);
		llegada = llegadaDao.create(llegada);
		llegada.setIdTipoTren(llegada.getTipoTren().getId().getId());
		
		assertTrue(llegadaDao.findByTipoTren(llegada.getTipoTren().getId().getId()) != null);
	}
}
