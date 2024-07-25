package ar.edu.unju.fi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pizza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PizzaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_Pizza",nullable = false)
	private Integer idPizza;
	@Column(nullable = false,length = 30,unique = true)
	private String name;
	@Column(nullable = false, length = 150)
	private String description;
	@Column(nullable = false,columnDefinition = "Decimal(5,2)")
	private Double price;
	@Column(columnDefinition = "TINYINT")
	private Boolean vegetarian;
	@Column(columnDefinition = "TINYINT")
	private boolean vegan;
	@Column(columnDefinition = "TINYINT",nullable = false)
	private Boolean avaible;
}
