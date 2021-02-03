package br.com.desafio.petz.api.service.conveter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.model.Pet;

@Service 
public class ClienteConveterImpl implements Converter<Cliente, ClienteDto> {
	
	@Autowired
	private Converter<Pet, PetDto> petConveter;
	
	/**
	 * Nesse converter o Dto não usa o Padrão Builder
	 */
	@Override
	public List<ClienteDto> convertListToListDto(List<Cliente> clientes) {
		if(clientes.isEmpty()) return new ArrayList<ClienteDto>(); 
		
		List<ClienteDto>  lista = null;
		lista = clientes.stream().map(cliente -> {
			return createNewDto(cliente);
			
		}).collect(Collectors.toList());
					
		return lista;
	}



	private ClienteDto createNewDto(Cliente cliente) {
		return new ClienteDto(cliente.getNome(),
				cliente.getEmail(),
				cliente.getDataNascimento());
//				convertListPetToListDto(getPets(cliente)));
	}



	private List<Pet> getPets(Cliente cliente) {
		if(cliente.getPets() == null) 
			return new ArrayList<Pet>();
		
		return cliente.getPets();
	}


//	private List<PetDto> convertListPetToListDto(List<Pet> lista) {
//		return petConveter.convertListToListDto(lista);
//	}
	
	

	@Override
	public ClienteDto convertToDto(Cliente cliente) {
		if (cliente == null)
			return new ClienteDto();

		return createNewDto(cliente);
	}

	

	@Override
	public Cliente converteDtoToEntity(ClienteDto dto) {
		return new Cliente(dto.getNome(), dto.getDataNascimento(), dto.getEmail());
	}

	@Override
	public List<Cliente> convertListDtoToListEntity(List<ClienteDto> dtos) {
		// TODO Auto-generated method stub
		return null;
	}

}
