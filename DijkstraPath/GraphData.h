//---------------------------------------------------------------------------
// GraphData.CPP
// Member function definitions for class Graph
// Author: Joel Richart
//---------------------------------------------------------------------------
// GraphData class:  
//   -- stores a description of the graph vertex
//   -- overloads the output operator to output the description

//---------------------------------------------------------------------------
#pragma once
#include <iostream>
#include <string>
using namespace std;
class GraphData
{
	friend ostream & operator<<(ostream &, const GraphData &);
public:
	GraphData();
	GraphData(GraphData &);
	GraphData(string);
	~GraphData();

private:
	//The stored data: a string representing the graph vertex description
	string vertexDesc;
};

