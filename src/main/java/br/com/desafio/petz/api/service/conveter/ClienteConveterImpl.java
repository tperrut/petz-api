package br.com.desafio.petz.api.service.conveter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.web.exception.BusinessException;

@Service 
public class ClienteConveterImpl implements Converter<Cliente, ClienteDto> {
	
	/**
	 * Nesse converter o Dto não usa o Padrão Builder
	 */
	@Override
	public List<ClienteDto> convertListToListDto(List<Cliente> clientes) {
		try {
			if(clientes.isEmpty()) return new ArrayList<ClienteDto>(); 
			
			List<ClienteDto>  lista = null;
			lista = clientes.stream().map(cliente -> {
				return createNewDto(cliente);
				
			}).collect(Collectors.toList());
						
			return lista;
		} catch (Exception e) {
			throw new BusinessException("ERRO NO CONVERTER -> convertListToListDto", e);

		}
	}



	private ClienteDto createNewDto(Cliente cliente) {
		return new ClienteDto(cliente.getNome(),
				cliente.getEmail(),
				cliente.getDataNascimento());
	}

	@Override
	public ClienteDto convertToDto(Cliente cliente) {
		if (cliente == null)
			return new ClienteDto();

		return createNewDto(cliente);
	}

	

	@Override
	public Cliente converteDtoToEntity(ClienteDto dto, Cliente cliente) {
		 try {
			return	createClienteForUpdate(dto,cliente);
		} catch (Exception e) {
			throw new BusinessException("ERRO NO CONVERTER -> converteDtoToEntity", e);
		}
	}
	
	@Override
	public Cliente converteDtoToEntity(ClienteDto dto) {
		 try {
			return	createClienteForSave(dto);
		} catch (Exception e) {
			throw new BusinessException("ERRO NO CONVERTER -> converteDtoToEntity", e);
		}
	}
	
	private Cliente createClienteForSave(ClienteDto dto) throws Exception {
		Cliente cliente = new Cliente(); 
		setValidFields(dto, cliente);
		return cliente;
	}
	
	private Cliente createClienteForUpdate(ClienteDto dto, Cliente cliente) throws Exception {
		setValidFields(dto, cliente);
		return cliente;
	}

	private void setValidFields(ClienteDto dto, Cliente cliente) {
		if(dto.getNome() != null) cliente.setNome(dto.getNome());
		if(dto.getEmail()!= null) cliente.setEmail(dto.getEmail());
		if(dto.getDataNascimento() != null) cliente.setDataNascimento(dto.getDataNascimento());
				
	}

}
