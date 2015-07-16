/*global angular */

'use strict';

describe('Unit: PessoasService', function() {

	var service;
	var httpBackend;

	beforeEach(function() {
		// instantiate the app module
		angular.mock.module('app');

		// mock the service
		angular.mock.inject(function(PessoasService, $httpBackend) {
			service = PessoasService;
			httpBackend = $httpBackend;
			httpBackend.when("GET", "languages/pt-br.json").respond({});
			httpBackend.when("GET", "rest/infoApp").respond({});
		});
	});

	it('Deveria existir o serviço', function() {
		expect(service).toBeDefined();
	});

	
	describe('Serviço deveria suportar listagem de pessoas', function() {
		
		it('Deveria ter um método de listagem', function() {
			expect(service.listar).toBeDefined();
		});

		it('O método de listagem deveria buscar dados no backend', function() {
			httpBackend.when('GET', 'rest/pessoas?max=10&pagina=1').respond({});
			httpBackend.expect('GET','rest/pessoas?max=10&pagina=1');
			service.listar(1, 10);
			httpBackend.flush();
		});

		it('O método de listagem deveria retornar os dados usando uma promise', function() {
			var resposta = {};
			var resultado;
			var flag;

			runs(function() {
				flag = false;
				httpBackend.when('GET', 'rest/pessoas?max=10&pagina=1').respond(resposta);
				service.listar(1, 10).then(function(data) {
					resultado = data;
					flag=true;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return flag;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(resultado).toEqual(resposta);
		    });

		});
		
		it('Em caso de erro na listagem deveria retornar o erro usando a promise',function(){
			var error = null;

			runs(function() {
				httpBackend.when('GET', 'rest/pessoas?max=10&pagina=1').respond(400,'ocorreu um erro');
				service.listar(1, 10).catch(function(err) {
					error = err;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return error !=null;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(error.data).toBeDefined();
				expect(error.status).toBeDefined();
		    });
		});

	});
	
	
	describe('Serviço deveria suportar recuperar uma pessoa', function() {
		
		it('Deveria ter um método de recuperar pessoa', function() {
			expect(service).toBeDefined();
		});
		
		it('O método de recuperação, deveria buscar dados no backend', function() {
			httpBackend.when('GET', 'rest/pessoas/1').respond({});
			httpBackend.expect('GET','rest/pessoas/1');
			service.recuperar(1);
			httpBackend.flush();
		});

		it('O método de recuperação, deveria retornar uma promise', function() {
			var resposta = {};
			var resultado;
			var flag;

			runs(function() {
				flag = false;
				httpBackend.when('GET', 'rest/pessoas/1').respond(resposta);
				service.recuperar(1).then(function(data) {
					resultado = data;
					flag=true;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return flag;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(resultado).toEqual(resposta);
		    });

		});
		
		it('Em caso de erro na recuperação deveria retornar o erro usando a promise',function(){
			var error = null;

			runs(function() {
				httpBackend.when('GET', 'rest/pessoas/1').respond(400,'ocorreu um erro');
				service.recuperar(1).catch(function(err) {
					error = err;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return error !=null;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(error.data).toBeDefined();
				expect(error.status).toBeDefined();
		    });
		});

	});

	
	describe('Serviço deveria suportar remover uma pessoa', function() {
			
		it('Deveria ter um método de remover pessoa', function() {
			expect(service.remover).toBeDefined();
		});
		
		it('O método de remoção, deveria usar o backend', function() {
			httpBackend.when('DELETE', 'rest/pessoas/1').respond({});
			httpBackend.expect('DELETE','rest/pessoas/1');
			service.remover({id:1});
			httpBackend.flush();
		});

		it('O método de remoção, deveria retornar uma promise', function() {
			var resposta = {};
			var resultado;
			var flag;

			runs(function() {
				flag = false;
				httpBackend.when('DELETE', 'rest/pessoas/1').respond(resposta);
				service.remover({id:1}).then(function(data) {
					resultado = data;
					flag=true;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return flag;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(resultado).toEqual(resposta);
		    });

		});
		
		it('Em caso de erro na remoção deveria retornar o erro usando a promise',function(){
			var error = null;

			runs(function() {
				httpBackend.when('DELETE', 'rest/pessoas/1').respond(400,'ocorreu um erro');
				service.remover({id:1}).catch(function(err) {
					error = err;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return error !=null;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(error.data).toBeDefined();
				expect(error.status).toBeDefined();
		    });
		});

	});

	
	describe('Serviço deveria suportar salvar uma pessoa', function() {
		
		it('Deveria ter um método de salvar pessoa', function() {
			expect(service.salvar).toBeDefined();
		});
		
		it('O método de salvamento, deveria usar o backend, para cadastrar novas pessoas', function() {
			httpBackend.when('POST', 'rest/pessoas/').respond({});
			httpBackend.expect('POST','rest/pessoas/');
			service.salvar({nome:'joao'});
			httpBackend.flush();
		});
		
		it('O método de salvamento, deveria usar o backend, para atualizar uma pessoa', function() {
			httpBackend.when('PUT', 'rest/pessoas/1').respond({});
			httpBackend.expect('PUT','rest/pessoas/1');
			service.salvar({id:1,nome:'joao'});
			httpBackend.flush();
		});

		it('O método de salvamento, deveria retornar uma promise', function() {
			var resposta = {};
			var resultado;
			var flag;

			runs(function() {
				flag = false;
				httpBackend.when('POST', 'rest/pessoas/').respond(resposta);
				service.salvar({nome:'joao'}).then(function(data) {
					resultado = data;
					flag=true;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return flag;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(resultado).toEqual(resposta);
		    });

		});
		
		it('Em caso de erro no salvamento deveria retornar o erro usando a promise',function(){
			var error = null;

			runs(function() {
				httpBackend.when('PUT', 'rest/pessoas/1').respond(400,'ocorreu um erro');
				service.salvar({id:1}).catch(function(err) {
					error = err;
				});
				httpBackend.flush();
			});
			
			waitsFor(function() {
		      return error !=null;
		    }, "A resposta deveria ter sido processada", 750);
			 
			runs(function() {
				expect(error.data).toBeDefined();
				expect(error.status).toBeDefined();
		    });
		});

	});

});