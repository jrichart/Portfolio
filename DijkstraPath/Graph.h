//---------------------------------------------------------------------------
// Graph.CPP
// Member function definitions for class Graph
// Author: Joel Richart
//---------------------------------------------------------------------------
// Graph class:  
//   -- points to a 'GraphData' class
//   -- stores the verticies and edges of a graph
//   -- computes the shortest path from each vertex to another
//   -- creates a formated output of the computed data
//   -- creates a formated output of one path
//   -- outputs the data held in 'GraphData'
//
// Assumptions:
//   -- 'GraphData' output operator provides formatted data output
//---------------------------------------------------------------------------
#pragma once

#include "GraphData.h"
#include <iostream>

using namespace std;

class Graph
{
public:
	//-------------------------- Constructor ------------------------------------
	// Default constructor for class Graph
	// Preconditions:   None
	// Postconditions:  None
	Graph();
	//---------------------------- Copy -----------------------------------------
	// Copy constructor for class Graph
	// Preconditions:   source.ptr points to an initialized Graph
	// Postconditions:  A deep copy of source is copied into *this
	Graph(Graph &);
	//---------------------------- Destructor -----------------------------------
	// Destructor for class Graph
	// Preconditions:   ptr points to memory on the heap
	// Postconditions:  All nodes of Graph are deallocated
	~Graph();

	//-------------------------------- buildGraph ---------------------------------
	// Builds a graph by reading data from an ifstream
	// Preconditions:  infile has been successfully opened and the file contains
	//                 properly formated data (according to the program specs)
	// Postconditions: One graph is read from infile and stored in the object
	void buildGraph(ifstream& infile);
	//-----------------------------  insertEdge  ----------------------------------
	// Inserts an edge to a specified vertex. It does not recompute paths
	// Preconditions:   A vertexnode should be created before adding edges
	// Postconditions:  A new edge node inserted into the specified VertexNode
	//		linked list, sorted by destination
	void insertEdge(int, int, int);
	//-----------------------------  removeEdge  ----------------------------------
	// Removes an edge from the source vertex 
	// Preconditions:   A vertexnode should be created before removing edges
	// Postconditions:  removes an edge from the specified source vertex while
	// maintaining the remaining edge pointers
	void removeEdge(int, int);
	//-----------------------------  displayAll  ----------------------------------
	// outputs the contents of the graph to show all possible shortest routes
	// Preconditions:   findShortestPath() has been run to create path information
	// Postconditions:  Possible paths are output by destination, on path per line
	void displayAll();
	//-----------------------------  display  -------------------------------------
	// outputs the shortest route between two verticies
	// Preconditions:   findShortestPath() has been run to create path information
	// Postconditions:  Shortest path is output as well as the destinations to get there
	void display(int, int);
	//-----------------------------  findShortestPath  ----------------------------
	// Determines the shortest path between all virtecies
	// Preconditions:   Multiple verticies and edges have populated the adjacency list
	// Postconditions:  Uses Dijkstra's algorithm to find the shortest weighted path
	// between two verticies and stores it in a two-dimentional array
	void findShortestPath();

private:
	// Use 101 as maximum, if you don't use vertex 0
	static const int MAX_VERTICES = 100;
	// Vertex description should not exceed specified size
	static const int MAX_SIZE = 50;

	struct EdgeNode {			// can change to a class, if desired
		int adjVertex;			// subscript of the adjacent vertex 
		int weight;				// weight of edge
		EdgeNode *nextEdge;
	};

	struct VertexNode {
		EdgeNode *edgeHead;		// head of the list of edges
		GraphData *data;		// store vertex data here
	};

	// array of VertexNodes
	VertexNode vertices[MAX_VERTICES];

	// table of information for Dijkstra's algorithm
	struct Table {
		bool visited;			// whether vertex has been visited
		int dist;				// shortest known distance from source
		int path;				// previous vertex in path of min dist
	};

	int size;					// number of vertices in the graph
	Table T[MAX_VERTICES][MAX_VERTICES];
	// stores visited, distance, path -
	// two dimensional in order to solve
	// for all sources

	//-----------------------------  printPath  -----------------------------------
	// Recursive Helper function to output the path from a source to a destination
	// Preconditions:   findShortestPath() has been run to create path information
	// Postconditions:  Each vertex in the path is separated by a space
	void printPath(int, int);
	//-----------------------------  printPathData  -----------------------------------
	// Recursive Helper function to output the path description 
	// from a source to a destination
	// Preconditions:   findShortestPath() has been run to create path information
	// Postconditions:  Each vertex in the path is separated by a new line
	void printPathData(int, int);
	//-----------------------------  placeEdge  -----------------------------------
	// A helper function to nsert an edge to a specified vertex. It does not 
	// recompute paths
	// Preconditions:   A vertexnode should be created before adding edges
	// Postconditions:  A new edge node inserted into the specified VertexNode
	//		linked list, sorted by destination
	void placeEdge(EdgeNode *, EdgeNode *);
};

