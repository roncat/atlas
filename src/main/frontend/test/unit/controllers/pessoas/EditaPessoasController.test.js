/*global angular */

'use strict';


describe('Unit: Pessoas - EditaPessoaController', function() {
	var controller;
	var PessoasService;
	var toaster;
	var defered;
	var rootScope;
	var factoryController;
	
	
	beforeEach(angular.mock.module('app'));
	
	beforeEach(inject(function($controller,$rootScope,$q,$httpBackend){
		factoryController = $controller;
		defered = $q;
		rootScope = $rootScope;
		PessoasService = jasmine.createSpyObj('PessoasService',['recuperar','salvar']);
		
		toaster = jasmine.createSpyObj('toaster',['pop']);

		$httpBackend.when("GET", "languages/pt-br.json").respond({});
		$httpBackend.when("GET", "rest/infoApp").respond({});

		
		PessoasService.recuperar.andReturn(defered.defer().promise);
		
		controller = factoryController('EditaPessoasCtrl',{
			PessoasService:PessoasService,
			toaster : toaster
		});
		
		rootScope.$apply();
	}));
	
	it('O controller deveria existir',function(){
		expect(controller).toBeDefined();
	});

	it('Ao editar um novo usuário deveria criar uma nova pessoa para edicao',function(){
		var params = {
			id:'novo'
		};

		PessoasService = jasmine.createSpyObj('PessoasService',['recuperar','salvar']);
		
		controller = factoryController('EditaPessoasCtrl',{
			PessoasService:PessoasService,
			toaster : toaster,
			$stateParams : params
		});
		
		expect(PessoasService.recuperar).not.toHaveBeenCalled();
		expect(controller.pessoa).toBeDefined();
	});
	
	it('Ao passar um id, o controller deve carregar essa pessoa no backend',function(){
	
		var params = {
			id:1
		};
		

		PessoasService.recuperar.andCallFake(function(){
			var def = defered.defer();
			def.resolve({id:1});
			return def.promise;
		});
		
		controller = factoryController('EditaPessoasCtrl',{
			PessoasService:PessoasService,
			toaster : toaster,
			$stateParams : params
		});
		rootScope.$apply();
		expect(PessoasService.recuperar).toHaveBeenCalledWith(1);
		expect(controller.pessoa.id).toEqual(1);
	});
	
	it('Deveria exibir uma mensagem amigavel, ao passar um id, o controller deve carregar essa pessoa no backend e dar erro na busca',function(){
		PessoasService.recuperar.andCallFake(function(){
			var def = defered.defer();
			def.reject();
			return def.promise;
		});
		
		controller = factoryController('EditaPessoasCtrl',{
			PessoasService:PessoasService,
			toaster : toaster
		});
		
		rootScope.$apply();
		
		expect(toaster.pop).toHaveBeenCalledWith('error','Sistema','Não foi possivel recuperar a pessoa informada');
	});
	
	it('Deveria poder salvar',function(){
		expect(controller.salvar).toBeDefined();
	});
	
	it('Deveria enviar solicitação de salvamento ao backend',function(){
		PessoasService.salvar.andReturn(defered.defer().promise);
	
		controller.salvar();
		
		expect(PessoasService.salvar).toHaveBeenCalled();
	});

	it('Deveria exibir uma mensagem de salvamento ao salvar com sucesso',function(){
		PessoasService.salvar.andCallFake(function(){
			var def = defered.defer();
			def.resolve();
			return def.promise;
		});
		
		controller.salvar();
		rootScope.$apply();

		expect(toaster.pop).toHaveBeenCalledWith('success','Sistema','Pessoa salva com sucesso');
	});
	
	it('Ao salvar caso ocorra erros de regra de negócio (http 409) deveria emitir mensagem amigável com o erro',function(){
		PessoasService.salvar.andCallFake(function(){
			var def = defered.defer();
			def.reject({
				status:409,
				data:{
					mensagem: 'erro'
				}
			});
			return def.promise;
		});
		
		controller.salvar();
		rootScope.$apply();

		expect(toaster.pop).toHaveBeenCalledWith('error','Sistema','erro');
	});

	it('Ao salvar caso ocorra erros de validação (http 412) deveria preencher esses erros no objeto de erro',function(){
		PessoasService.salvar.andCallFake(function(){
			var def = defered.defer();
			def.reject({
				status:412,
				data:{nome:'informe o nome'}
			});
			return def.promise;
		});
		
		controller.salvar();
		rootScope.$apply();

		expect(controller.erros).toEqual({nome:'informe o nome'});
	});

	
	it('Ao salvar caso ocorra erros não reconhecidos avisar o usuário do erro',function(){
		PessoasService.salvar.andCallFake(function(){
			var def = defered.defer();
			def.reject({
				status:500
			});
			return def.promise;
		});
		
		controller.salvar();
		rootScope.$apply();

		expect(toaster.pop).toHaveBeenCalledWith('error','Sistema','ocorreu um erro ao salvar');;
	});
	
});