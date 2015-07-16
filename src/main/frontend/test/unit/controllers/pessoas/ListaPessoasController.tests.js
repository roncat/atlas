/*global angular */

'use strict';


describe('Unit: Pessoas - ListaPessoasController', function() {
	var controller;
	var PessoasService;
	var toaster;
	var defered;
	var rootScope;
	
	
	beforeEach(angular.mock.module('app'));
	
	beforeEach(inject(function($controller,$rootScope,$q,$httpBackend){
		
		
		defered = $q;
		rootScope = $rootScope;
		PessoasService = jasmine.createSpyObj('PessoasService',['listar','remover']);
		toaster = jasmine.createSpyObj('toaster',['pop']);

		$httpBackend.when("GET", "languages/pt-br.json").respond({});
		$httpBackend.when("GET", "rest/infoApp").respond({});

		PessoasService.listar.andReturn(defered.defer().promise);
		
		controller = $controller('ListaPessoasCtrl',{
			PessoasService:PessoasService,
			toaster : toaster
		});
	}));
	
	beforeEach(function(){
		spyOn(window, 'confirm');
	});
	
	it('controller deveria existir',function(){
		expect(controller).toBeDefined();
	});
	
	it('o controller deveria ter um objeto pessoa com as configurações básicas de uma lista',function(){
		expect(controller.pessoas).toBeDefined();
		expect(controller.pessoas.items).toBeDefined();
		expect(controller.pessoas.count).toBeDefined();
		expect(controller.pessoas.pagina).toBeDefined();
		expect(controller.pessoas.maxResults).toBeDefined();

		expect(controller.pessoas.items).toEqual([]);
		expect(controller.pessoas.count).toEqual(0);
		expect(controller.pessoas.pagina).toEqual(1);
		expect(controller.pessoas.maxResults).toEqual(10);

	});
	
	it('O método de atualizar lista de pessoas deveria existir',function(){
		expect(controller.atualizar).toBeDefined();
	});
	
	it('Ao criar o controller a pesquisa de pessoas já deveria ter sido executada',function(){
		expect(PessoasService.listar).toHaveBeenCalledWith(1,10);
	});
	
	it('Ao atualizar deveria consultar PessoasService paginando',function(){
		controller.atualizar();
		expect(PessoasService.listar).toHaveBeenCalledWith(1,10);
	});

	
	it('Em caso de erro na listagem deveria exibir mensagem amigável',function(){
		
		PessoasService.listar.andCallFake(function(){
			var def = defered.defer();
			def.reject({data:409, status:'erro qualquer'});
			return def.promise;
		});
		controller.atualizar();

		// disgest
		rootScope.$apply();
		
		expect(toaster.pop).toHaveBeenCalledWith('error','Sistema',"Não foi possivel recuperar a listagem de pessoas");
		
	});
	
	it('Ao efetuar a busca deveria preencher a lista de pessoas com os dados retornados configurando paginação',function(){
		PessoasService.listar.andCallFake(function(){
			var def = defered.defer();
			def.resolve({items:[{},{},{}],count:3,pagina:1,maxResults:10});
			return def.promise;
		});
		controller.atualizar();

		// disgest
		rootScope.$apply();
		
		expect(controller.pessoas.items).toEqual([{},{},{}]);
		expect(controller.pessoas.count).toEqual(3);
		expect(controller.pessoas.pagina).toEqual(1);
		expect(controller.pessoas.maxResults).toEqual(10);
		
	});
	
	it('O método de remover pessoa deveria existir',function(){
		expect(controller.remover).toBeDefined();
	});
	
	it('O método de remoção deveria fazer uma confirmação antes de remover',function(){
		window.confirm.andReturn(false);
		
		controller.remover({id:1});
		
		expect(window.confirm).toHaveBeenCalled();
	});
	
	it('O método de remoção somente poderia remover no backend com uma confirmação positiva',function(){
		window.confirm.andReturn(false);
		
		controller.remover({id:1});
		expect(PessoasService.remover).not.toHaveBeenCalled();
		
		PessoasService.remover.andReturn(defered.defer().promise);
		window.confirm.andReturn(true);
		controller.remover({id:1});
		expect(PessoasService.remover).toHaveBeenCalled();
	});
	
	it('Ao remover deveria atualizar a lista de pessoas',function(){
		PessoasService.remover.andCallFake(function(){
			var def = defered.defer();
			def.resolve();
			return def.promise;
		});
		
		spyOn(controller,'atualizar');
		window.confirm.andReturn(true);
		controller.remover({id:1});
		rootScope.$apply();

		expect(controller.atualizar).toHaveBeenCalled();
	});
	
	it('Ao remover uma pessoa em caso de sucesso deveria ser exibida uma mensagem para o usuário',function(){
		
		PessoasService.remover.andCallFake(function(){
			var def = defered.defer();
			def.resolve();
			return def.promise;
		});
		
		window.confirm.andReturn(true);
		controller.remover({id:1});
		rootScope.$apply();

		expect(toaster.pop).toHaveBeenCalledWith('success', 'Sistema', "Pessoa removida com sucesso");
	});
	
	it('Ao remover uma pessoa em caso de falha na remoção o usuário deveria receber uma mensagem amigável com o erro',function(){
		PessoasService.remover.andCallFake(function(){
			var def = defered.defer();
			def.reject();
			return def.promise;
		});
		
		window.confirm.andReturn(true);
		controller.remover({id:1});
		rootScope.$apply();

		expect(toaster.pop).toHaveBeenCalledWith('error', 'Sistema', "Não foi possivel remover a pessoa");
	});
	
	
	
	
	


});