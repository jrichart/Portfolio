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

#include "Graph.h"
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

//-------------------------- Constructor ------------------------------------
// Default constructor for class Graph
// Preconditions:   None
// Postconditions:  None
Graph::Graph()
{
}

//---------------------------- Copy -----------------------------------------
// Copy constructor for class Graph
// Preconditions:   source.ptr points to an initialized Graph
// Postconditions:  A deep copy of source is copied into *this
Graph::Graph(Graph &source)
{
	size = source.size;

	for (int i = 0; i < size; i++) {
		GraphData *point = new GraphData(*source.vertices[i].data);
		VertexNode *vertex = new VertexNode;
		vertex->data = point;
		vertex->edgeHead = NULL;
		vertices[i] = *vertex;
	}

	for (int i = 0; i < size; i++) {
		EdgeNode *pNode = source.vertices[i].edgeHead;
		while (pNode != NULL)
		{
			int src = i;
			int dest = pNode->adjVertex;
			int cost = pNode->weight;
			insertEdge(src, dest, cost);
			pNode = pNode->nextEdge;
		}
	}
}

//---------------------------- Destructor -----------------------------------
// Destructor for class Graph
// Preconditions:   ptr points to memory on the heap
// Postconditions:  All nodes of Graph are deallocated
Graph::~Graph()
{
	for (int i = 0; i < size; i++)
	{
		// destroy EdgeNode.
		EdgeNode *pNode = vertices[i].edgeHead;
		while (pNode != NULL)
		{
			EdgeNode *oldEdge = pNode;
			pNode = pNode->nextEdge;
			delete oldEdge;
		}

		// destory data
		delete vertices[i].data;
	}
}

//-------------------------------- buildGraph ---------------------------------
// Builds a graph by reading data from an ifstream
// Preconditions:  infile has been successfully opened and the file contains
//                 properly formated data (according to the program specs)
// Postconditions: One graph is read from infile and stored in the object
void Graph::buildGraph(ifstream& infile) {

	infile >> size;                          // data member stores array size
	if (infile.eof())
		return;
	infile.ignore();                         // throw away '\n' go to next line

	// get descriptions of vertices
	for (int v = 1; v <= size; v++) {
		string description;
		getline(infile, description, '\n');	// read descriptions (use of this method is not mandatory)
		GraphData *point = new GraphData(description);
		VertexNode *vertex = new VertexNode;
		vertex->data = point;
		vertex->edgeHead = NULL;
		vertices[v - 1] = *vertex;            // store appropriately
	}

	// fill cost edge array
	int src = 1, dest = 1, cost = 1;
	for (;;) {
		infile >> src >> dest >> cost;
		if (src == 0 || infile.eof())
			break;
		insertEdge(src, dest, cost);
	}
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			T[i][j].dist = -1;
			T[i][j].path = -1;
			T[i][j].visited = false;
		}
	}

}

//-----------------------------  insertEdge  ----------------------------------
// Inserts an edge to a specified vertex. It does not recompute paths
// Preconditions:   A vertexnode should be created before adding edges
// Postconditions:  A new edge node inserted into the specified VertexNode
//		linked list, sorted by destination
void Graph::insertEdge(int source, int destination, int cost)
{
	EdgeNode *newEdge = new EdgeNode;
	newEdge->adjVertex = destination;
	newEdge->weight = cost;
	newEdge->nextEdge = NULL;

	if (source > size) {
		cout << "Error: Vertex Not Found" << endl;
	}
	else if (vertices[source - 1].edgeHead == NULL) {
		vertices[source - 1].edgeHead = newEdge;
		return;
	}
	else if (vertices[source - 1].edgeHead->adjVertex > newEdge->adjVertex){
		newEdge->nextEdge = vertices[source - 1].edgeHead;
		vertices[source - 1].edgeHead = newEdge;
		return;
	}
	else {
		placeEdge(newEdge, vertices[source - 1].edgeHead);
	}
}

//-----------------------------  placeEdge  -----------------------------------
// A helper function to nsert an edge to a specified vertex. It does not 
// recompute paths
// Preconditions:   A vertexnode should be created before adding edges
// Postconditions:  A new edge node inserted into the specified VertexNode
//		linked list, sorted by destination
void Graph::placeEdge(EdgeNode *newEdge, EdgeNode *root)
{
	if (root->adjVertex < newEdge->adjVertex && root->nextEdge == NULL) {
		root->nextEdge = newEdge;
		return;
	}
	else if (root->adjVertex < newEdge->adjVertex && root->nextEdge->adjVertex > newEdge->adjVertex) {
		newEdge->nextEdge = root->nextEdge;
		root->nextEdge = newEdge;
		return;
	}
	else {
		placeEdge(newEdge, root->nextEdge);
	}
}

//-----------------------------  removeEdge  ----------------------------------
// Removes an edge from the source vertex 
// Preconditions:   A vertexnode should be created before removing edges
// Postconditions:  removes an edge from the specified source vertex while
// maintaining the remaining edge pointers
void Graph::removeEdge(int source, int destination)
{
	 
	if (vertices[source - 1].edgeHead == NULL) {
		cout << "No Edges Associated With This Vertex" << endl;
		return;
	}
	EdgeNode *pNode = new EdgeNode;
	EdgeNode *tempNode = new EdgeNode;
	pNode->nextEdge = vertices[source - 1].edgeHead;

	while (pNode != NULL) {
		if (pNode->adjVertex == destination) {
			tempNode = pNode->nextEdge;
			pNode->nextEdge = tempNode;
			delete tempNode;
		}
		pNode = pNode->nextEdge;
	}

}

//-----------------------------  displayAll  ----------------------------------
// outputs the contents of the graph to show all possible shortest routes
// Preconditions:   findShortestPath() has been run to create path information
// Postconditions:  Possible paths are output by destination, on path per line
void Graph::displayAll()
{
	cout << "Description\t\t\tFrom\tTo\tDistance\tPath" << endl; 
	EdgeNode *pNode = new EdgeNode;

	for (int c = 0; c < size; c++) {
		pNode = vertices[c].edgeHead;
		cout << *vertices[c].data << endl;

		for (int i = 0; i < size; i++) {
			if (c != i) {
				cout << "\t\t\t\t" << c + 1 << "\t" << i + 1 << "\t";
				if (T[c][i].dist < 0) {
					cout << "--";
				}
				else {
					cout << T[c][i].dist << "\t\t";
					printPath(i, c);
				}
				cout << endl;
			}

		}
	}
	delete pNode;
}

//-----------------------------  printPath  -----------------------------------
// Recursive Helper function to output the path from a source to a destination
// Preconditions:   findShortestPath() has been run to create path information
// Postconditions:  Each vertex in the path is separated by a space
void Graph::printPath(int vertex, int source)
{
	if (vertex == source) {
		cout << source + 1 << " ";
	}
	else {
		printPath(T[source][vertex].path, source);
		cout << vertex + 1 << " ";
	}
}

//-----------------------------  printPathData  -----------------------------------
// Recursive Helper function to output the path description 
// from a source to a destination
// Preconditions:   findShortestPath() has been run to create path information
// Postconditions:  Each vertex in the path is separated by a new line
void Graph::printPathData(int vertex, int source)
{
	EdgeNode *pNode = new EdgeNode;
	if (vertex == source) {
		pNode = vertices[source].edgeHead;
		cout << *vertices[source].data << endl;
	}
	else {
		printPathData(T[source][vertex].path, source);
		pNode = vertices[vertex].edgeHead;
		cout << *vertices[vertex].data << endl;
	}
	delete pNode;
}

//-----------------------------  findShortestPath  ----------------------------
// Determines the shortest path between all virtecies
// Preconditions:   Multiple verticies and edges have populated the adjacency list
// Postconditions:  Uses Dijkstra's algorithm to find the shortest weighted path
// between two verticies and stores it in a two-dimentional array
void Graph::findShortestPath()
{
	EdgeNode *pNode = new EdgeNode;
	for (int i = 0; i < size; i++){
		T[i][i].dist = 0;
		T[i][i].path = 0;
		for (int j = 0; j < size; j++) {
			int v = 0;
			//Check all destination vertex distances
			for (int k = 1; k < size; k++) {
				//Make sure they are positive and haven't previously been visited
				if (T[i][k].dist >= 0 && T[i][k].visited == false) {
					//Find the smallest distance available
					if (T[i][k].dist < T[i][v].dist || T[i][v].visited == true || 
						T[i][v].dist < 0) {
						v = k;
					}
				}
			}
			T[i][v].visited = true;
			pNode = vertices[v].edgeHead;
			//This is the only way I could figure out how to have vertexes without
			//edges not fill out part of the table. It seems too specific and not
			//good practice
			if (pNode == NULL && v == i) {
				break;
			}
			while (pNode != NULL){
				if (T[i][pNode->adjVertex - 1].dist < 0) {
					T[i][pNode->adjVertex - 1].dist = pNode->weight + T[i][v].dist;
					T[i][pNode->adjVertex - 1].path = v;
				}

				else if (T[i][pNode->adjVertex - 1].visited == false &&
						pNode->weight + T[i][v].dist < T[i][pNode->adjVertex - 1].dist) {
					T[i][pNode->adjVertex - 1].dist = pNode->weight + T[i][v].dist;
					T[i][pNode->adjVertex - 1].path = v;
				}
				pNode = pNode->nextEdge;
			}
		}
	}
	delete pNode;
}

//-----------------------------  display  -------------------------------------
// outputs the shortest route between two verticies
// Preconditions:   findShortestPath() has been run to create path information
// Postconditions:  Shortest path is output as well as the destinations to get there
void Graph::display(int vertex1, int vertex2)
{
	cout << vertex1 << "\t" << vertex2 << "\t";
	if (T[vertex1 - 1][vertex2 - 1].dist < 0) {
		cout << "--" << "No Path Is Available";
	}
	else {
		cout << T[vertex1 - 1][vertex2 - 1].dist << "\t";
		printPath(vertex2 - 1, vertex1 - 1);
		cout << endl;
		printPathData(vertex2 - 1, vertex1 - 1);
	}
	cout << endl;
}
