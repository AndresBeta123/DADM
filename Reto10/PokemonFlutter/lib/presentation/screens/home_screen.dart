import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:dio/dio.dart';
import 'package:http/http.dart' as http;
import 'package:http_app/infrastructure/models/pokemon.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  Pokemon? pokemon;
  int pokemonId = 1;

  @override
  void initState() {
    super.initState();
    getPokemon();
  }

  Future<void> getPokemon() async {
    final response = await Dio().get('https://pokeapi.co/api/v2/pokemon/$pokemonId');
    pokemon = Pokemon.fromJson(response.data);
    setState((){});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('peticion'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment:MainAxisAlignment.center,
          children: [
          Text(pokemon?.name ?? 'fallo'),
          if(pokemon != null)
            Image.network(pokemon!.sprites.frontDefault),
        ],)
      ),
      floatingActionButton: Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              FloatingActionButton(
                child: const Icon(Icons.navigate_next),
                onPressed: () {
                  pokemonId = pokemonId + 1;
                  getPokemon();
                },
              ),
              SizedBox(height: 16), // Espaciado entre los dos botones flotantes
              FloatingActionButton(
                child: const Icon(Icons.navigate_before),
                onPressed: () {
                  pokemonId = pokemonId - 1;
                  getPokemon();
                },
              ),
            ],
      ),
    );
  }
}
